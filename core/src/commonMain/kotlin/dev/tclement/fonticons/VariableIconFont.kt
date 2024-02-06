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

package dev.tclement.fonticons

import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight

internal abstract class VariableIconFont : IconFont() {
    abstract val variationSettings: Array<out FontVariation.Setting>
    abstract val opticalSizePreset: Boolean
    abstract fun textStyleWeightFor(weight: FontWeight): FontWeight
}
