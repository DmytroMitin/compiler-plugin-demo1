package compilerPlugin

import scala.reflect.internal.util.BatchSourceFile
import scala.tools.nsc.io.VirtualDirectory
import scala.tools.nsc.reporters.ConsoleReporter
import scala.tools.nsc.{Global, Settings}

object AnnotationFinderTest extends App {
  // prepare the code you want to compile
//  val code = "object Foo extends Application { println(42 / 0) }"
  val code =
    """
      |class Typestate(filename:String) extends scala.annotation.StaticAnnotation
      |
      |@Typestate(filename = "MyProtocol.txt")
      |class Cat{
      |  def comeAlive(): Unit = println("The cat is alive")
      |}
      |
      |object Main extends App {
      |  val cat = new Cat()
      |  cat.comeAlive()
      |}""".stripMargin
  val sources = List(new BatchSourceFile("<test>", code))
  println("sources "+sources)
  val settings = new Settings
  settings.usejavacp.value = true

  settings.outputDirs.setSingleOutput(new VirtualDirectory("(memory)", None))

  val compiler = new Global(settings, new ConsoleReporter(settings)) {
    override protected def computeInternalPhases () {
      super.computeInternalPhases
      for (phase <- new GetFileFromAnnotation(this).components)
        phasesSet += phase
    }
  }
  /**
  val compiler = new Global(settings, new ConsoleReporter(settings)) {
    override protected def loadRoughPluginsList: List[Plugin] =
      new GetFileFromAnnotation(this) :: super.loadRoughPluginsList
  }
   **/

  new compiler.Run() compileSources (sources)

}