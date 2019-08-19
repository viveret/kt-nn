package com.viveret.tinydnn.data.repo

import android.content.Context
import com.viveret.tinydnn.basis.JsonStreamPackage
import com.viveret.tinydnn.basis.StreamPackage
import org.json.JSONObject

class JsonRepo(val context: Context, val json: JSONObject): Repo {
    constructor(context: Context, json: String): this(context, JSONObject(json))

    override val name: String = json.getString("name")
    override val version: Int = json.getInt("version")
    override val packages: List<StreamPackage>
        get() {
            val array = json.getJSONArray("packages")
            return List(array.length()) {
                val item = array.getJSONObject(it)
                JsonStreamPackage(context, item)
            }
        }
}