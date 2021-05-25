import cats.data.OptionT
import cats.implicits._
import mainargs.{ Flag, ParserForClass, TokensReader, arg, main }
import org.eclipse.jgit.lib.RepositoryBuilder
import org.eclipse.jgit.revwalk.RevWalk

import java.text.MessageFormat
import java.util.{ Locale, ResourceBundle }
import scala.util.{ Try, Using }

// https://wiki.eclipse.org/JGit/User_Guide#API
// https://stackoverflow.com/questions/12342152/jgit-and-finding-the-head
// https://github-api.kohsuke.org/

object Main extends App {

  private val logger = org.log4s.getLogger

  // format: OFF
  @main(name = "metrics-scala", doc = "Scala-based metrics experiments")
  case class Options(
    @arg(name = "repo", short = 'r', doc = "repository path")
      repo: os.Path
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

  println(s"repo = $repoPath")

  val builder = new RepositoryBuilder
  val repo = builder.setGitDir((repoPath / ".git").toIO)
    .readEnvironment()
    .findGitDir()
    .build()

  println(s">>>>> ${repo.resolve("HEAD")}")

  val walk = new RevWalk(repo)
  val commit = walk.parseCommit(repo.resolve("HEAD"))
  println(s">>>>> ${commit.getAuthorIdent}")

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
