//import cats.data.OptionT
//import cats.implicits._
import mainargs.{ Flag, ParserForClass, TokensReader, arg, main }
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.RepositoryBuilder
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.TreeWalk

import java.text.MessageFormat
import java.util.{ Locale, ResourceBundle }
import scala.util.{ Try, Using }
import scala.jdk.CollectionConverters._ // provides .asScala on Java types

object GitImport extends App {

  private val logger = org.log4s.getLogger

  private val dbFilename = "git-import.sqlite"

  // format: OFF
  @main(name = "git-import", doc = "Git importer for Scala-based metrics experiments - currently just displays code size per commit")
  case class Options(
    @arg(name = "repo", short = 'r', doc = "local repository path (defaults to .)")
      repo: os.Path = os.pwd,
    @arg(name = "database", short = 'd', doc = "local SQLite (defaults to ./git-import.sqlite)")
      database: os.Path = os.pwd / dbFilename,
    @arg(name = "totals-only", short = 't', doc = "show only total repo size per commit")
      totalsOnly: Flag = Flag(false)
//    @arg(name = "--issues", short = 'i')
//      issues: Flag = Flag(true)
  )
  // format: ON

  implicit object PathRead extends TokensReader[os.Path](
    "path",
    strs => Right(os.Path(strs.head, os.pwd)))
  val options = ParserForClass[Options].constructOrExit(args.toIndexedSeq)
  logger.info(args.toString)
  logger.info(options.toString)

  val repoPath = options.repo
  logger.info(f"repoPath = $repoPath")

  val dbPath = options.database
  logger.info(f"dbPath = $dbPath")

  // Externalized resource bundle in src/main/resources.
  val bundle = ResourceBundle.getBundle("messages", Locale.US)
  logger.debug("loaded resource bundle")

  // compute the number of options present to check mutual exclusion, not counting database path
  val numOptions = -1 + productToMap(options).count {
    case (_, None) => false
    case (_, Flag(false)) => false
    case _ => true
  }
  logger.debug(s"number of options besides database: ${numOptions}")

  val dotGit = ".git"
  val revisedRepoPath = if (repoPath.last == dotGit) repoPath else repoPath / dotGit
  println(s"repo = $revisedRepoPath")

  val totalsOnly = options.totalsOnly.value

  val builder = new RepositoryBuilder
  val repo = builder.setGitDir((repoPath / ".git").toIO)
    .readEnvironment()
    .findGitDir()
    .build()

  val walk = new RevWalk(repo)
  val head = walk.parseCommit(repo.resolve("HEAD"))
  println(s"head = $head")
  println(s"default branch = ${repo.getBranch}")

  val branches = new Git(repo).branchList().call().asScala.map { b => b.getName }
  println(s"all branches = $branches")

  walk.reset()
  walk.markStart(head)
  var previousTime = System.currentTimeMillis()
  walk.forEach { c =>
    println(s"${c.getId.abbreviate(7).name} on ${c.getAuthorIdent.getWhen}")
    val w = new TreeWalk(repo)
    w.addTree(c.getTree)
    w.setRecursive(true)
    // TODO convert to functional iterator
    var sum = 0L
    while (w.next()) {
      val id = w.getObjectId(0)
      val ol = repo.open(id)
      val size = ol.getSize
      if (!totalsOnly) println(s"  ${w.getPathString} $size")
      sum += size
    }
    val currentTime = System.currentTimeMillis()
    val totalTime = (currentTime - previousTime).toDouble / 1000
    previousTime = currentTime
    println(f"  TOTAL $sum bytes - ${totalTime}s")
  }

  def productToMap(cc: Product) = cc.productElementNames.zip(cc.productIterator).toMap

  def printMessageFormat(key: String, args: Any*): Unit = {
    val form = new MessageFormat(bundle.getString(key))
    println(form.format(args.toArray))
  }

  // conversion of Flag to Option to support for comprehensions
  implicit class FlagToOption(val self: Flag) {
    val toOption: Option[Boolean] = if (self.value) Some(true) else None
  }
}
