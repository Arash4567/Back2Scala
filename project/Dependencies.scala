import sbt.*

object Dependencies {
  object Versions {
    lazy val http4s = "0.23.21"
    lazy val logback = "1.4.8"
    lazy val skunk = "0.6.0"
    lazy val enumeratum = "1.7.2"
  }

  trait LibGroup {
    def all: Seq[ModuleID]
  }

  object org {
    object http4s extends LibGroup {
      def http4s(artifact: String): ModuleID =
        "org.http4s" %% s"http4s-$artifact" % Versions.http4s

      lazy val core = http4s("core")
      lazy val server = http4s("ember-server")
      lazy val dsl = http4s("dsl")

      override def all: Seq[sbt.ModuleID] = Seq(core, server, dsl)
    }

    object tpolecat {
      object skunk {
        lazy val core = "org.tpolecat" %% "skunk-core" % Versions.skunk
      }
    }
  }
  object ch {
    object qos {
      lazy val logback = "ch.qos.logback" % "logback-classic" % Versions.logback
    }
  }

  object com {
    object beachape {
      object enumeratum extends LibGroup {
        def enumeratum(artifact: String): ModuleID =
          "com.beachape" %% artifact % Versions.enumeratum

        lazy val core = enumeratum("enumeratum")
        lazy val cats = enumeratum("enumeratum-cats")
        lazy val circe = enumeratum("enumeratum-circe")

        lazy val all: Seq[sbt.ModuleID] = Seq(core, cats, circe)
      }
    }
  }
}
