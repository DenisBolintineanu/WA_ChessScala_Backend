package modules

import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}
import services.IPersistenceService
import services.local.LocalPersistenceService

class PersistenceServiceModule extends Module {
  def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq(bind[IPersistenceService].to[LocalPersistenceService])
  }
}
