package com.viveret.tinydnn.basis

class DataSlice: HashMap<DataRole, Pair<Vect, Long>> {
    constructor(vararg params: Pair<DataRole, Pair<Vect, Long>>): super() {
        for (p in params) {
            this[p.first] = p.second
        }
    }

    constructor(params: List<Pair<DataRole, Pair<Vect, Long>>>): super() {
        for (p in params) {
            this[p.first] = p.second
        }
    }
}