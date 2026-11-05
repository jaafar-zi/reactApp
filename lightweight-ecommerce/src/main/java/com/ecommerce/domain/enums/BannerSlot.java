package com.ecommerce.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BannerSlot {

    // ─── BRAND assets ─────────────────────────────────────────
    BRAND_HOME_LOGO             ("BRAND", 1L,  "Home page — brand section logo"),
    BRAND_HOME_COMMUNITY_LOGO   ("BRAND", 2L,  "Home page — community section logo"),
    BRAND_ABOUT_COMMUNITY_LOGO  ("BRAND", 3L,  "About page — community section logo"),

    // ─── HOME page ────────────────────────────────────────────
    HOME_ABOUT_IMAGE            ("BANNER", 1L,  "Home page — about section image"),
    HOME_COLUMN_PRODUCTS        ("BANNER", 10L, "Home page — three-column: products column image"),
    HOME_COLUMN_COMMUNITY       ("BANNER", 11L, "Home page — three-column: community column image"),
    HOME_COLUMN_STORY           ("BANNER", 12L, "Home page — three-column: story column image"),
    HOME_COMMUNITY_IMAGE        ("BANNER", 20L, "Home page — community section image"),

    // ─── HOME gallery slider ──────────────────────────────────
    HOME_GALLERY_SLIDE_1        ("BANNER", 30L, "Home page — media showcase slider: slide 1"),
    HOME_GALLERY_SLIDE_2        ("BANNER", 31L, "Home page — media showcase slider: slide 2"),
    HOME_GALLERY_SLIDE_3        ("BANNER", 32L, "Home page — media showcase slider: slide 3"),
    HOME_GALLERY_SLIDE_1_MOBILE ("BANNER_MOBILE", 30L, "Home page — media showcase slider: slide 1 mobile"),
    HOME_GALLERY_SLIDE_2_MOBILE ("BANNER_MOBILE", 31L, "Home page — media showcase slider: slide 2 mobile"),
    HOME_GALLERY_SLIDE_3_MOBILE ("BANNER_MOBILE", 32L, "Home page — media showcase slider: slide 3 mobile"),

    // ─── HOME hero slider ─────────────────────────────────────
    HERO_SLIDE_1                ("HERO", 1L, "Home page — hero slider: slide 1 (hero_03.jpg)"),
    HERO_SLIDE_2                ("HERO", 2L, "Home page — hero slider: slide 2 (hero_02.jpg)"),
    HERO_SLIDE_3                ("HERO", 3L, "Home page — hero slider: slide 3 (hero_01.jpg)"),
    HERO_SLIDE_1_MOBILE         ("HERO_MOBILE", 1L, "Home page — hero slider: slide 1 mobile"),
    HERO_SLIDE_2_MOBILE         ("HERO_MOBILE", 2L, "Home page — hero slider: slide 2 mobile"),
    HERO_SLIDE_3_MOBILE         ("HERO_MOBILE", 3L, "Home page — hero slider: slide 3 mobile"),

    // ─── ABOUT page ───────────────────────────────────────────
    ABOUT_HERO_IMAGE            ("BANNER", 40L, "About page — hero section image"),
    ABOUT_IMAGE_SECTION         ("BANNER", 41L, "About page — image section"),
    ABOUT_RIGHT_IMAGE           ("BANNER", 42L, "About page — split content right image"),

    // ─── ABOUT flickthrough ───────────────────────────────────
    ABOUT_FLICKTHROUGH_1        ("BANNER", 50L, "About page — flickthrough image 1"),
    ABOUT_FLICKTHROUGH_2        ("BANNER", 51L, "About page — flickthrough image 2"),
    ABOUT_FLICKTHROUGH_3        ("BANNER", 52L, "About page — flickthrough image 3"),

    // ─── ABOUT sticky gallery ─────────────────────────────────
    ABOUT_STICKY_1              ("BANNER", 60L, "About page — sticky gallery item 1 (top-left)"),
    ABOUT_STICKY_2              ("BANNER", 61L, "About page — sticky gallery item 2 (center-left)"),
    ABOUT_STICKY_3              ("BANNER", 62L, "About page — sticky gallery item 3 (top-right)"),
    ABOUT_STICKY_4              ("BANNER", 63L, "About page — sticky gallery item 4 (center-bottom)"),

    // ─── ABOUT split grid ─────────────────────────────────────
    ABOUT_SPLIT_GRID_MAIN       ("BANNER", 70L, "About page — split grid main fixed image"),
    ABOUT_SPLIT_GRID_FLICK_1    ("BANNER", 71L, "About page — split grid flickthrough 1"),
    ABOUT_SPLIT_GRID_FLICK_2    ("BANNER", 72L, "About page — split grid flickthrough 2"),
    ABOUT_SPLIT_GRID_FLICK_3    ("BANNER", 73L, "About page — split grid flickthrough 3"),
    ABOUT_SPLIT_GRID_FLICK_4    ("BANNER", 74L, "About page — split grid flickthrough 4"),
    ABOUT_SPLIT_GRID_FLICK_5    ("BANNER", 75L, "About page — split grid flickthrough 5"),

    // ─── MAIL templates ───────────────────────────────────────
    MAIL_ORDER_HEADER_LOGO      ("BRAND",  10L,  "Mail — order confirmation header logo"),
    MAIL_WELCOME_HEADER_LOGO    ("BRAND",  11L,  "Mail — welcome/subscription header logo"),
    MAIL_FOOTER_LOGO            ("BRAND",  12L,  "Mail — shared footer logo (both templates)"),

    // ─── LEGAL pages ──────────────────────────────────────────
    LEGAL_SHOWCASE_IMAGE        ("BANNER", 110L, "Shared legal pages image above footer");

    private final String ownerType;
    private final Long   ownerId;
    private final String description;

    public AssetOwnerType getAssetOwnerType() {
        return AssetOwnerType.valueOf(this.ownerType);
    }
}