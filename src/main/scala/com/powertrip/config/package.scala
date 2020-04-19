package com.powertrip

import eu.timepit.refined.W
import eu.timepit.refined.api._
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.MinSize

package object config {

  type DatabasePassword = String Refined MinSize[W.`0`.T]
}
