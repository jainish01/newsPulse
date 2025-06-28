package com.example.newsapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ArticleDetailScreen(article: Article, onOpenInBrowser: (String) -> Unit, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.urlToImage)
                .crossfade(true)
                .error(R.drawable.fallback_image)
                .placeholder(R.drawable.fallback_image)
                .fallback(R.drawable.fallback_image)
                .build(),
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        article.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = article.source?.name ?: "Unknown Source",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.End),
        )

        article.publishedAt?.let {
            Text(
                text = formatDate(it),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.End),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        article.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        article.content?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        article.author?.let {
            Text(
                text = "Author: $it",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        article.url?.let {
            ArticleLinkText(it, onOpenInBrowser)
        }
    }
}

fun formatDate(publishedAt: String): String {
    return try {
        val instant = Instant.parse(publishedAt)
        val localDateTime = instant.atZone(ZoneId.systemDefault())
        localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
    } catch (_: Exception) {
        publishedAt
    }
}

@Composable
fun ArticleLinkText(url: String, onOpenInBrowser: (String) -> Unit) {

    val annotatedText = buildAnnotatedString {
        append("Read more at:\n")

        withLink(
            LinkAnnotation.Clickable(
                tag = "article_url",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            ) {
                onOpenInBrowser(url)
            }
        ) {
            append(url)
        }
    }

    Text(
        text = annotatedText,
        modifier = Modifier,
    )
}


