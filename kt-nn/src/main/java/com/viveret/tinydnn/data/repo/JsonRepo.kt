package com.viveret.tinydnn.data.repo

import com.viveret.tinydnn.basis.JsonStreamPackage
import com.viveret.tinydnn.basis.StreamPackage
import org.json.JSONObject

class JsonRepo(val json: JSONObject): Repo {
    constructor(json: String): this(JSONObject(json))

    override val name: String = json.getString("name")
    override val version: Int = json.getInt("version")
    override val packages: List<StreamPackage>
        get() {
            val array = json.getJSONArray("packages")
            return List(array.length()) {
                val item = array.getJSONObject(it)
                JsonStreamPackage(item)
            }
        }
}