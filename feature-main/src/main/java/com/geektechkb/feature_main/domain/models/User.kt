package com.geektechkb.feature_main.domain.models


import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.geektechkb.core.base.BaseDiffModel
import kotlinx.serialization.json.JsonObject

@kotlinx.serialization.Serializable
data class User(
    val name: String? = null,
    val lastName: String? = null,
    override val phoneNumber: String? = null,
    val profileImage: String? = null,
    val lastSeen: String? = null,
    val isPhoneNumberHidden: Boolean? = null,
    override val _highlightResult: JsonObject? = null,
) : BaseDiffModel, Highlightable {
    val highlightedName: HighlightedString?
        get() = getHighlight(Attribute("name"))
}