package dev.materii.gloom.domain.manager.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.materii.gloom.shared.R

enum class AppIcon(
    val aliasName: String,
    @StringRes val iconName: Int,
    @StringRes val iconDescription: Int,
    @DrawableRes val preview: Int,
    val collection: AppIconCollection
) {

    // Classic

    Main(
        aliasName = "gloom.icons.classic.Main",
        iconName = R.string.app_icon_main,
        iconDescription = R.string.app_icon_main_description,
        preview = R.drawable.gloom_icon_default,
        collection = AppIconCollection.Classic
    ),

    Sky(
        aliasName = "gloom.icons.classic.Sky",
        iconName = R.string.app_icon_sky,
        iconDescription = R.string.app_icon_sky_description,
        preview = R.drawable.gloom_icon_sky,
        collection = AppIconCollection.Classic
    ),

    Light(
        aliasName = "gloom.icons.classic.Light",
        iconName = R.string.app_icon_light,
        iconDescription = R.string.app_icon_light_description,
        preview = R.drawable.gloom_icon_light,
        collection = AppIconCollection.Classic
    ),

    // Stylized

    Blueprint(
        aliasName = "gloom.icons.stylized.Blueprint",
        iconName = R.string.app_icon_blueprint,
        iconDescription = R.string.app_icon_blueprint_description,
        preview = R.drawable.gloom_icon_blueprint,
        collection = AppIconCollection.Stylized
    ),

    // Pride

    Pride(
        aliasName = "gloom.icons.pride.LGBT",
        iconName = R.string.app_icon_pride,
        iconDescription = R.string.app_icon_pride_description,
        preview = R.drawable.gloom_icon_pride,
        collection = AppIconCollection.Pride
    ),

    Trans(
        aliasName = "gloom.icons.pride.Trans",
        iconName = R.string.app_icon_trans,
        iconDescription = R.string.app_icon_trans_description,
        preview = R.drawable.gloom_icon_trans,
        collection = AppIconCollection.Pride
    ),

    TransInverted(
        aliasName = "gloom.icons.pride.TransInverted",
        iconName = R.string.app_icon_trans_inverted,
        iconDescription = R.string.app_icon_trans_inverted_description,
        preview = R.drawable.gloom_icon_trans_inverted,
        collection = AppIconCollection.Pride
    ),

    // Catppuccin

    Mocha(
        aliasName = "gloom.icons.catppuccin.Mocha",
        iconName = R.string.app_icon_mocha,
        iconDescription = R.string.app_icon_mocha_description,
        preview = R.drawable.gloom_icon_mocha,
        collection = AppIconCollection.Catppuccin
    ),

    Macchiato(
        aliasName = "gloom.icons.catppuccin.Macchiato",
        iconName = R.string.app_icon_macchiato,
        iconDescription = R.string.app_icon_macchiato_description,
        preview = R.drawable.gloom_icon_macchiato,
        collection = AppIconCollection.Catppuccin
    ),

    Frappe(
        aliasName = "gloom.icons.catppuccin.Frappe",
        iconName = R.string.app_icon_frappe,
        iconDescription = R.string.app_icon_frappe_description,
        preview = R.drawable.gloom_icon_frappe,
        collection = AppIconCollection.Catppuccin
    ),

    Latte(
        aliasName = "gloom.icons.catppuccin.Latte",
        iconName = R.string.app_icon_latte,
        iconDescription = R.string.app_icon_latte_description,
        preview = R.drawable.gloom_icon_latte,
        collection = AppIconCollection.Catppuccin
    ),

    // Holiday 2025

    Valentines25(
        aliasName = "gloom.icons.holiday25.Valentines",
        iconName = R.string.app_icon_valentines25,
        iconDescription = R.string.app_icon_valentines25_description,
        preview = R.drawable.gloom_icon_valentines25,
        collection = AppIconCollection.Holiday25
    )

}

enum class AppIconCollection(
    @StringRes val nameRes: Int
) {

    Classic(R.string.app_icon_collection_classic),
    Stylized(R.string.app_icon_collection_stylized),
    Pride(R.string.app_icon_collection_pride),
    Catppuccin(R.string.app_icon_collection_catppuccin),
    Holiday25(R.string.app_icon_collection_holiday25)

}