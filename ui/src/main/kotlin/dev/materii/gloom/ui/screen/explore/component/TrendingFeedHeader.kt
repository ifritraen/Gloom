package dev.materii.gloom.ui.screen.explore.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.materii.gloom.domain.manager.PreferenceManager
import dev.materii.gloom.domain.manager.enums.TrendingPeriodPreference
import dev.materii.gloom.shared.R
import dev.materii.gloom.ui.component.filter.ChoiceInputChip

@Composable
fun TrendingFeedHeader(
    currentTrendingPeriod: TrendingPeriodPreference,
    onTrendingPeriodChange: (TrendingPeriodPreference) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.title_trending),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )

        ChoiceInputChip(
            defaultValue = PreferenceManager.TRENDING_PERIOD,
            currentValue = currentTrendingPeriod,
            onSelectChoice = onTrendingPeriodChange,
            label = { period ->
                Text(
                    stringResource(
                        when (period) {
                            TrendingPeriodPreference.MONTHLY -> R.string.label_trending_monthly
                            TrendingPeriodPreference.WEEKLY -> R.string.label_trending_weekly
                            TrendingPeriodPreference.DAILY -> R.string.label_trending_daily
                        }
                    )
                )
            }
        )
    }
}