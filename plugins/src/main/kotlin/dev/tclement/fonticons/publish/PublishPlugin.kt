/*
 * Copyright 2024 T. Clément (@tclement0922)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.tclement.fonticons.publish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.konan.properties.loadProperties

class PublishPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("org.gradle.maven-publish")

            val libProperties = loadProperties(rootDir.absolutePath + "/library.properties")

            group = System.getenv("GROUP") ?: libProperties["group"] as? String
                    ?: error("Key 'group' not found in library.properties")
            version = System.getenv("VERSION") ?: libProperties["version"] as String?
                    ?: error("Key 'version' not found in library.properties")

            configure<PublishingExtension> {
                publications {
                    repositories {
                        if (file(rootDir.absolutePath + "/github.properties").exists()) {
                            val githubProperties = loadProperties(rootDir.absolutePath + "/github.properties")
                            maven {
                                name = "GitHubPackages"
                                url = uri(
                                    libProperties["packages-url"] as? String
                                        ?: error("Key 'packages-url' not found in library.properties")
                                )
                                credentials {
                                    username = githubProperties["username"] as? String
                                        ?: error("Key 'username' not found in github.properties")
                                    password = githubProperties["token"] as? String
                                        ?: error("Key 'token' not found in github.properties")
                                }
                            }
                        } else if (System.getenv("GH_PKG_USERNAME") != null && System.getenv("GH_PKG_TOKEN") != null) {
                            maven {
                                name = "GitHubPackages"
                                url = uri(
                                    libProperties["packages-url"] as? String
                                        ?: error("Key 'packages-url' not found in library.properties")
                                )
                                credentials {
                                    username = System.getenv("GH_PKG_USERNAME")
                                    password = System.getenv("GH_PKG_TOKEN")
                                }
                            }
                        }
                    }
                    if (!plugins.hasPlugin("multiplatform-structure")) {
                        register<MavenPublication>("release") {
                            afterEvaluate {
                                from(components.getByName("release"))
                            }
                        }
                    }
                }
            }
        }
    }
}