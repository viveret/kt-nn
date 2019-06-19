package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.Stream
import java.io.InputStream

class DataSelection : HashMap<String, DataSelection.Item>() {
    class Item(val info: Stream, val stream: InputStream)
}