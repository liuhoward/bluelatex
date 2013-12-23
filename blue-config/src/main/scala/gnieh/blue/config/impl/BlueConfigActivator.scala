/*
 * This file is part of the \BlueLaTeX project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gnieh.blue
package config
package impl

import org.osgi.framework._

import org.slf4j.LoggerFactory
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator

import java.io.File

/** Register the configuration loader service that is used by everybody
 *
 *  @author Lucas Satabin
 */
class BlueConfigActivator extends BundleActivator {

  import common.FileProcessing._

  def start(context: BundleContext): Unit = {
    val configBase = new File(context.getProperty("blue.configuration.base"))
    // the bundle configuration loader server
    val loader = new ConfigurationLoaderImpl(configBase)
    // register it
    context.registerService(classOf[ConfigurationLoader], loader, null)
    // configure the logging framework
    val loggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    try {
      val configurator = new JoranConfigurator
      configurator.setContext(loggerContext)
      configurator.doConfigure(configBase / "logback.xml")
    } catch {
      case e: Exception =>
        // TODO what to do?
    }
  }

  def stop(context: BundleContext): Unit = {
  }

}
