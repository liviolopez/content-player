package com.liviolopez.contentplayer.data.remote.response

import com.liviolopez.contentplayer.data.local.model.Item

/** TODO()
 * fetch items from remote source
 */
data class ItemDto(
    val url: String,
    val format: String,
    val drmUuid: String?,
    val drmLicense: String?,
)

// Mapper with Kotlin Extension (Better performance in comparison of Kotlin Reflect)
fun ItemDto.toLocalModel() = Item(
    url = url,
    format = format,
    drmUuid = drmUuid,
    drmLicense = drmLicense
)

// Mapper with Kotlin Reflect (This code was added here as a sample)
// can be used when the class to be mapped has a large number of params
// fun ItemDto.toLocalModel() = with(::Item) {
//    val propertiesByName = ItemDto::class.memberProperties.associateBy { it.id }
//
//    callBy(args = parameters.associateWith { parameter ->
//        when (parameter.name) {
//            "price" -> price.replace("$","").trim().toFloat()
//            else -> propertiesByName[parameter.name]?.get(this@toLocalModel)
//        }
//    })
// }