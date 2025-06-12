// File: app/src/main/java/com/ibrahim/scamdetect/ml/Vectorizer.kt
package com.ibrahim.scamdetect.ml

import android.content.Context
import org.json.JSONObject
import java.nio.charset.StandardCharsets

object Vectorizer {
    private lateinit var vocab: Map<String, Int>
    private lateinit var idf: FloatArray

    // Hard‑coded English stopwords (standard NLTK set)
    private val stopWords: Set<String> = setOf(
        "a","about","above","after","again","against","all","am","an","and","any","are","aren't",
        "as","at","be","because","been","before","being","below","between","both","but","by",
        "can't","cannot","could","couldn't","did","didn't","do","does","doesn't","doing","don't",
        "down","during","each","few","for","from","further","had","hadn't","has","hasn't","have",
        "haven't","having","he","he'd","he'll","he's","her","here","here's","hers","herself","him",
        "himself","his","how","how's","i","i'd","i'll","i'm","i've","if","in","into","is","isn't",
        "it","it's","its","itself","let's","me","more","most","mustn't","my","myself","no","nor",
        "not","of","off","on","once","only","or","other","ought","our","ours","ourselves","out",
        "over","own","same","shan't","she","she'd","she'll","she's","should","shouldn't","so",
        "some","such","than","that","that's","the","their","theirs","them","themselves","then",
        "there","there's","these","they","they'd","they'll","they're","they've","this","those",
        "through","to","too","under","until","up","very","was","wasn't","we","we'd","we'll","we're",
        "we've","were","weren't","what","what's","when","when's","where","where's","which","while",
        "who","who's","whom","why","why's","with","won't","would","wouldn't","you","you'd","you'll",
        "you're","you've","your","yours","yourself","yourselves"
    )

    /**
     * Call once on app startup to load the TF‑IDF vocabulary & IDF from your JSON asset.
     */
    fun initFromAsset(context: Context, assetName: String) {
        val jsonText = context.assets.open(assetName).use { stream ->
            String(stream.readBytes(), StandardCharsets.UTF_8)
        }
        val root = JSONObject(jsonText)
        // Build vocabulary map
        vocab = mutableMapOf<String, Int>().also { map ->
            val vocObj = root.getJSONObject("vocabulary")
            vocObj.keys().forEach { token ->
                map[token] = vocObj.getInt(token)
            }
        }
        // Build IDF float array
        val idfJson = root.getJSONArray("idf")
        idf = FloatArray(idfJson.length()) { i ->
            idfJson.getDouble(i).toFloat()
        }
    }

    /**
     * Transform raw text → TF‑IDF vector.
     * Steps:
     * 1) regex tokenize on \\b\\w+\\b
     * 2) lowercase & filter stopwords
     * 3) raw counts
     * 4) tf = count/total × idf
     */
    fun transform(text: String): FloatArray {
        val tokens = Regex("\\b\\w+\\b")
            .findAll(text.lowercase())
            .map { it.value }
            .filter { it.isNotEmpty() }
            .toList()

        val filtered = tokens.filter { it !in stopWords }

        // raw counts
        val counts = IntArray(idf.size)
        filtered.forEach { tok ->
            vocab[tok]?.let { idx ->
                counts[idx] += 1
            }
        }

        // TF‑IDF
        val total = filtered.size.coerceAtLeast(1)
        return FloatArray(idf.size) { i ->
            (counts[i].toFloat() / total) * idf[i]
        }
    }
}
