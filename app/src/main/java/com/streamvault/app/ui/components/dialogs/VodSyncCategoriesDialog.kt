package com.streamvault.app.ui.components.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.OutlinedButton
import com.streamvault.app.R
import com.streamvault.app.ui.interaction.TvClickableSurface
import com.streamvault.app.ui.screens.settings.VodSyncVerification
import com.streamvault.app.ui.screens.settings.VodSyncVerificationStatus
import com.streamvault.app.ui.screens.settings.summaryText
import com.streamvault.app.ui.theme.OnSurface
import com.streamvault.app.ui.theme.OnSurfaceDim
import com.streamvault.app.ui.theme.Primary
import com.streamvault.app.ui.theme.PrimaryLight
import com.streamvault.app.ui.theme.SurfaceElevated
import com.streamvault.app.ui.theme.SurfaceHighlight

data class VodCategorySelectItem(
    val id: Long,
    val name: String,
    val count: Int = 0
)

@Composable
fun VodSyncCategoriesDialog(
    categories: List<VodCategorySelectItem>,
    selectedIds: Set<Long>,
    onToggle: (Long) -> Unit,
    onSelectAll: () -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit,
    verification: VodSyncVerification?,
    onVerify: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumDialog(
        title = stringResource(R.string.vod_sync_categories_title),
        subtitle = "${categories.size} categories available",
        onDismissRequest = onDismiss,
        widthFraction = 0.55f,
        heightFraction = 0.85f,
        bodyHeightFraction = 0.75f,
        modifier = modifier,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onSelectAll,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.vod_sync_categories_select_all),
                        color = Primary
                    )
                }
                OutlinedButton(
                    onClick = onClearAll,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.vod_sync_categories_clear_all),
                        color = OnSurfaceDim
                    )
                }
            }

            VodSyncVerificationRow(
                verification = verification,
                onVerify = onVerify
            )

            categories.forEach { category ->
                VodCategorySelectRow(
                    category = category,
                    isSelected = category.id in selectedIds,
                    onToggle = { onToggle(category.id) }
                )
            }
        },
        footer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PremiumDialogFooterButton(
                    label = stringResource(R.string.vod_sync_categories_cancel),
                    onClick = onDismiss
                )
                PremiumDialogFooterButton(
                    label = stringResource(R.string.vod_sync_categories_done),
                    onClick = onDismiss,
                    emphasized = true
                )
            }
        }
    )
}

@Composable
private fun VodSyncVerificationRow(
    verification: VodSyncVerification?,
    onVerify: () -> Unit
) {
    val statusText = verification?.summaryText()
        ?: "Tap Verify to check whether selected categories finished syncing."
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodySmall,
            color = if (verification?.status == VodSyncVerificationStatus.VERIFIED) Primary else OnSurfaceDim,
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(onClick = onVerify) {
            Text(text = "Verify", color = Primary)
        }
    }
}

@Composable
private fun VodCategorySelectRow(
    category: VodCategorySelectItem,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    TvClickableSurface(
        onClick = onToggle,
        modifier = Modifier.fillMaxWidth(),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isSelected) Primary.copy(alpha = 0.15f) else SurfaceElevated.copy(alpha = 0.4f),
            focusedContainerColor = SurfaceHighlight
        ),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(2.dp, PrimaryLight),
                shape = RoundedCornerShape(8.dp)
            )
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Primary,
                    uncheckedColor = OnSurfaceDim
                )
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurface,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
            if (category.count > 0) {
                Text(
                    text = "(${category.count})",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceDim
                )
            }
        }
    }
}
