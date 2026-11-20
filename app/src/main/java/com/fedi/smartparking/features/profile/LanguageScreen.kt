package com.fedi.smartparking.features.profile

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fedi.smartparking.R
import com.fedi.smartparking.ui.theme.Navy
import com.fedi.smartparking.ui.theme.PrimaryBlue
import java.util.Locale

private const val LANGUAGE_PREFERENCES = "language_preferences"
private const val LANGUAGE_TAG = "language_tag"

@Composable
fun LanguageScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity
    val currentLanguage = remember {
        context.getSharedPreferences(LANGUAGE_PREFERENCES, Context.MODE_PRIVATE)
            .getString(LANGUAGE_TAG, "en")
            .orEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.explore_back),
                    tint = Navy,
                )
            }
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text(
                    text = stringResource(R.string.language_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Navy,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.language_subtitle),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        LanguageOption(
            label = stringResource(R.string.language_english),
            tag = "en",
            selected = currentLanguage == "en",
            onClick = {
                selectLanguage(context, "en")
                activity?.recreate()
            },
        )
        LanguageOption(
            label = stringResource(R.string.language_french),
            tag = "fr",
            selected = currentLanguage == "fr",
            onClick = {
                selectLanguage(context, "fr")
                activity?.recreate()
            },
        )
        LanguageOption(
            label = stringResource(R.string.language_arabic),
            tag = "ar",
            selected = currentLanguage == "ar",
            onClick = {
                selectLanguage(context, "ar")
                activity?.recreate()
            },
        )
    }
}

@Composable
private fun LanguageOption(
    label: String,
    tag: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = if (selected) PrimaryBlue.copy(alpha = 0.1f) else Color.White,
        shape = RoundedCornerShape(16.dp),
        border = if (selected) {
            androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue)
        } else {
            null
        },
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, color = Navy, fontWeight = FontWeight.Bold)
                Text(tag.uppercase(Locale.ROOT), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (selected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryBlue)
            }
        }
    }
}

fun applySavedLanguage(context: Context) {
    val tag = context.getSharedPreferences(LANGUAGE_PREFERENCES, Context.MODE_PRIVATE)
        .getString(LANGUAGE_TAG, "en")
        .orEmpty()
    applyLanguage(context, tag)
}

private fun selectLanguage(context: Context, tag: String) {
    context.getSharedPreferences(LANGUAGE_PREFERENCES, Context.MODE_PRIVATE)
        .edit()
        .putString(LANGUAGE_TAG, tag)
        .apply()
    applyLanguage(context, tag)
}

private fun applyLanguage(context: Context, tag: String) {
    val locale = Locale.forLanguageTag(tag)
    Locale.setDefault(locale)
    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(locale)
    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
}
