-- =====================================================================
-- JAWISTA E-COMMERCE — DATA POPULATION SCRIPT
-- Collection 01 — El Mediterraneo
-- Products: 4 | Languages: en, fr, ar
-- =====================================================================
-- Tables populated:
--   1. products
--   2. product_variants
--   3. product_pricing
--   4. inventory
--   5. product_attributes
--   6. attribute_values
--   7. attribute_key_translations
-- Note: product_assets will be populated later
-- =====================================================================


-- =====================================================================
-- 1. PRODUCTS
-- =====================================================================

INSERT INTO products (id, sku, name_key, description_key, active, created_at, updated_at)
VALUES
(1, 'JAW-TS-CHILLBRO-WHITE', 'product.chill_bro_tshirt_white.name',   'product.chill_bro_tshirt_white.description',   true, NOW(), NOW()),
(2, 'JAW-RKT-CLUB-NATURAL',  'product.jawista_club_racket_set.name',  'product.jawista_club_racket_set.description',  true, NOW(), NOW()),
(3, 'JAW-RKT-PRO-NATURAL',   'product.jawista_pro_racket_set.name',   'product.jawista_pro_racket_set.description',   true, NOW(), NOW()),
(4, 'JAW-BALL-SET-BLACK',    'product.jawista_ball_set_black.name',   'product.jawista_ball_set_black.description',   true, NOW(), NOW());


-- =====================================================================
-- 2. PRODUCT VARIANTS (T-shirt sizes only)
-- =====================================================================

INSERT INTO product_variants (id, product_id, sku, size, active, created_at, updated_at)
VALUES
(1, 1, 'JAW-TS-CHILLBRO-WHITE-S',   'S',   true, NOW(), NOW()),
(2, 1, 'JAW-TS-CHILLBRO-WHITE-M',   'M',   true, NOW(), NOW()),
(3, 1, 'JAW-TS-CHILLBRO-WHITE-L',   'L',   true, NOW(), NOW()),
(4, 1, 'JAW-TS-CHILLBRO-WHITE-XL',  'XL',  true, NOW(), NOW()),
(5, 1, 'JAW-TS-CHILLBRO-WHITE-XXL', 'XXL', true, NOW(), NOW());


-- =====================================================================
-- 3. PRODUCT PRICING
-- =====================================================================

INSERT INTO product_pricing (id, product_id, base_price, sale_price, currency, tax_rate)
VALUES
(1, 1, 75.00, NULL, 'TND', 0.00),
(2, 2, 65.00, NULL, 'TND', 0.00),
(3, 3, 65.00, NULL, 'TND', 0.00),
(4, 4, 10.00, NULL, 'TND', 0.00);


-- =====================================================================
-- 4. INVENTORY
-- =====================================================================
-- Product 1 : inventory per variant (product_id = NULL)
-- Products 2,3,4 : inventory per product (variant_id = NULL)

INSERT INTO inventory (id, product_id, variant_id, quantity, reserved_quantity, min_stock_level, last_updated)
VALUES
(1, NULL, 1, 10, 0, 5,  NOW()),
(2, NULL, 2, 15, 0, 5,  NOW()),
(3, NULL, 3, 15, 0, 5,  NOW()),
(4, NULL, 4, 5,  0, 5,  NOW()),
(5, NULL, 5, 5,  0, 5,  NOW()),
(6, 2,    NULL, 30,  0, 5,  NOW()),
(7, 3,    NULL, 30,  0, 5,  NOW()),
(8, 4,    NULL, 100, 0, 10, NOW());


-- =====================================================================
-- 5. PRODUCT ATTRIBUTES + 6. ATTRIBUTE VALUES
-- =====================================================================


-- ─────────────────────────────────────────────────────────────────────
-- PRODUCT 1: Chill Bro T-shirt [White]
-- SKU: JAW-TS-CHILLBRO-WHITE
-- ─────────────────────────────────────────────────────────────────────

-- ── name ─────────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (1, 1, 'name', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (1, 1, 'Chill Bro T-shirt [White]');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (2, 1, 'name', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (2, 2, 'Chill Bro T-shirt [Blanc]');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (3, 1, 'name', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (3, 3, 'Chill Bro T-shirt [أبيض]');

-- ── collection ───────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (4, 1, 'collection', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (4, 4, 'Collection 01 El Mediterraneo');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (5, 1, 'collection', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (5, 5, 'Collection 01 El Mediterraneo');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (6, 1, 'collection', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (6, 6, 'Collection 01 El Mediterraneo');

-- ── description ──────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (7, 1, 'description', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (7,  7, 'Our newest white T-shirt arrives in a relaxed, signature Jawista fit made for everyday ease.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (8,  7, 'The front is understated, detailed with a subtle black embroidery at the chest.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (9,  7, 'On the back, a bold graphic reminds us: not everything needs a reaction, just slow down and live the moment.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (8, 1, 'description', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (10, 8, 'Une pièce essentielle, pensée dans une coupe ample et décontractée, signature Jawista.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (11, 8, 'Un équilibre naturel entre confort et allure, conçu pour accompagner le quotidien sans effort.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (12, 8, 'À l''avant, une présence discrète, une broderie noire, subtilement posée.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (13, 8, 'Au dos, un message plus affirmé : un rappel silencieux que tout ne mérite pas de réaction.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (14, 8, 'Ralentir. Ressentir. Vivre l''instant.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (9, 1, 'description', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (15, 9, 'قطعة أساسية، بتصميم واسع ومريح، تحمل توقيع Jawista.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (16, 9, 'توازن طبيعي بين الراحة والأناقة، صُمّم ليواكب يومك بكل بساطة.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (17, 9, 'في الأمام، حضور هادئ، تطريز أسود دقيق يضيف لمسة خفية.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (18, 9, 'في الخلف، رسالة أوضح: تذكير صامت بأن ليس كل شيء يستحق ردّة فعل.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (19, 9, 'تمهّل. اشعر. عِش اللحظة.');

-- ── badge ────────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (10, 1, 'badge', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (20, 10, 'Locally made | Limited drop.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (11, 1, 'badge', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (21, 11, 'Fabriqué localement | Édition limitée');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (12, 1, 'badge', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (22, 12, 'مصمّم ومصنوع محليًا | كميات محدودة.');

-- ── material ─────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (13, 1, 'material', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (23, 13, '100% Cotton');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (14, 1, 'material', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (24, 14, 'Coton 100% premium');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (15, 1, 'material', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (25, 15, 'قطن 100% عالي الجودة');

-- ── product_details ──────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (16, 1, 'product_details', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (26, 16, 'Screen Print & Embroidery Branding');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (27, 16, 'Locally designed & produced in Tunisia');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (17, 1, 'product_details', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (28, 17, 'Marquage en sérigraphie & broderie');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (29, 17, 'Conception et production locales, en Tunisie');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (18, 1, 'product_details', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (30, 18, 'طباعة وتطريز بعناية');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (31, 18, 'تصميم وإنتاج محلي في تونس');

-- ── washing_instructions ─────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (19, 1, 'washing_instructions', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (32, 19, 'Machine wash cold with like colors');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (33, 19, 'Gentle cycle');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (34, 19, 'Use mild detergent');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (35, 19, 'Do not bleach');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (20, 1, 'washing_instructions', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (36, 20, 'À froid, avec des couleurs similaires');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (37, 20, 'Cycle délicat');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (38, 20, 'Lessive douce');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (39, 20, 'Sans agents blanchissants');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (21, 1, 'washing_instructions', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (40, 21, 'على درجة حرارة منخفضة مع ألوان مشابهة');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (41, 21, 'دورة غسيل لطيفة');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (42, 21, 'استخدام منظف خفيف');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (43, 21, 'بدون مواد مبيّضة');

-- ── drying_instructions ──────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (22, 1, 'drying_instructions', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (44, 22, 'Tumble dry low / hang to dry');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (45, 22, 'Warm iron if needed (not on branding)');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (46, 22, 'Do not dry clean');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (23, 1, 'drying_instructions', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (47, 23, 'À basse température ou à l''air libre');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (48, 23, 'Repassage à température modérée si nécessaire (éviter les zones marquées)');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (49, 23, 'Ne pas nettoyer à sec');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (24, 1, 'drying_instructions', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (50, 24, 'على حرارة منخفضة أو يُترك ليجف في الهواء');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (51, 24, 'يمكن الكي بدرجة حرارة متوسطة عند الحاجة (تجنّب المناطق المطبوعة أو المطرّزة)');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (52, 24, 'يُمنع التنظيف الجاف');


-- ─────────────────────────────────────────────────────────────────────
-- PRODUCT 2: Kids & Beginners Beach Racket Set - Jawista Club
-- SKU: JAW-RKT-CLUB-NATURAL
-- ─────────────────────────────────────────────────────────────────────

-- ── name ─────────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (25, 2, 'name', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (53, 25, 'Kids & Beginners Beach Racket Set - Jawista Club [Natural wood]');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (26, 2, 'name', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (54, 26, 'Set de Raquettes de Plage Kids & Débutants · Jawista Club [Bois naturel]');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (27, 2, 'name', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (55, 27, 'Kids & Beginners Beach Racket Set Jawista Club [خشب طبيعي]');

-- ── collection ───────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (28, 2, 'collection', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (56, 28, 'Collection 01 El Mediterraneo');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (29, 2, 'collection', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (57, 29, 'Collection 01 El Mediterraneo');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (30, 2, 'collection', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (58, 30, 'Collection 01 El Mediterraneo');

-- ── description ──────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (31, 2, 'description', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (59, 31, 'Designed for first swings, shared moments, and easy fun under the sun.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (60, 31, 'The Jawista Club set includes two lightweight plywood rackets, built to maximize exchanges and keep the game flowing.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (61, 31, 'Soft rubber handles provide a comfortable grip, making it easy to play longer without effort.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (62, 31, 'Paired with two Jawista balls, this set is made for smooth rallies and instant enjoyment.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (63, 31, 'Lightweight and easy to handle, it''s the perfect entry point for beginners, families, and kids, helping everyone build confidence, improve coordination, and enjoy the game before stepping up to more advanced play.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (64, 31, 'A simple way to start. A natural way to progress.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (32, 2, 'description', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (65, 32, 'Pensé pour les premiers échanges, les moments partagés et le plaisir simple sous le soleil.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (66, 32, 'Le set Jawista Club accompagne les débuts avec légèreté, fluidité et naturel.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (67, 32, 'Composé de deux raquettes en contreplaqué, légères et faciles à manier, il favorise des échanges continus et un jeu accessible.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (68, 32, 'Les poignées en caoutchouc souple offrent une prise confortable, permettant de jouer plus longtemps, sans effort.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (69, 32, 'Associé à deux balles Jawista, l''ensemble est conçu pour des échanges fluides et un plaisir immédiat.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (70, 32, 'Léger et intuitif, c''est une entrée idéale pour les enfants, les débutants et les moments en famille, développer la coordination, prendre confiance, et simplement apprécier le jeu, avant d''aller plus loin.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (71, 32, 'Un début simple. Une progression naturelle.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (33, 2, 'description', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (72, 33, 'مصمّمة للبدايات الأولى، للحظات المشتركة، وللمتعة البسيطة تحت الشمس.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (73, 33, 'ترافق مجموعة Jawista Club خطواتك الأولى بخفّة، وانسيابية، وبشكل طبيعي.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (74, 33, 'تتكوّن من مضربين من الخشب الرقائقي، خفيفين وسهلين في الاستخدام، ما يسهّل تبادل الكرات ويجعل اللعب أكثر سلاسة.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (75, 33, 'توفر المقابض المطاطية الناعمة راحة في الإمساك، لتتمكن من اللعب لفترة أطول دون جهد.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (76, 33, 'ومع كرتين من Jawista، صُمّمت المجموعة لتمنحك تبادلات سلسة ومتعة فورية.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (77, 33, 'خفيفة وبديهية، هي الخيار المثالي للأطفال، للمبتدئين، وللحظات العائلية، لتنمية التنسيق، وبناء الثقة، والاستمتاع باللعب قبل الانتقال إلى مستوى أعلى.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (78, 33, 'بداية بسيطة. تقدّم طبيعي.');

-- ── badge ────────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (34, 2, 'badge', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (79, 34, 'Locally made | Limited drop.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (35, 2, 'badge', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (80, 35, 'Conçu et fabriqué localement | édition limitée.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (36, 2, 'badge', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (81, 36, 'مصمّمة ومصنوعة محليًا | إصدار محدود.');

-- ── material ─────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (37, 2, 'material', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (82, 37, 'Lightweight Plywood');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (38, 2, 'material', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (83, 38, 'Raquettes en contreplaqué léger');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (39, 2, 'material', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (84, 39, 'مضارب من الخشب الرقائقي الخفيف');

-- ── dimensions ───────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (40, 2, 'dimensions', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (85, 40, '38 × 23.5 cm');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (41, 2, 'dimensions', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (86, 41, '38 × 23,5 cm');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (42, 2, 'dimensions', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (87, 42, '38 × 23.5 سم');

-- ── product_details ──────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (43, 2, 'product_details', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (88, 43, '2 Rackets Included');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (89, 43, 'Soft Rubber Grip Handles');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (90, 43, '2 Jawista Balls Included');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (91, 43, 'Locally designed & produced in Tunisia');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (44, 2, 'product_details', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (92, 44, 'Set de 2 raquettes');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (93, 44, 'Poignées en caoutchouc souple');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (94, 44, '2 balles Jawista incluses');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (95, 44, 'Conçu et produit localement en Tunisie');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (45, 2, 'product_details', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (96, 45, 'مجموعة من مضربين');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (97, 45, 'مقابض مطاطية ناعمة');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (98, 45, 'تتضمن كرتين من Jawista');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (99, 45, 'تصميم وإنتاج محلي في تونس');


-- ─────────────────────────────────────────────────────────────────────
-- PRODUCT 3: Advanced Beach Racket Set - Jawista Pro
-- SKU: JAW-RKT-PRO-NATURAL
-- ─────────────────────────────────────────────────────────────────────

-- ── name ─────────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (46, 3, 'name', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (100, 46, 'Advanced Beach Racket Set - Jawista Pro [Natural wood]');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (47, 3, 'name', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (101, 47, 'Set de Raquettes de Plage Jawista Pro [Bois naturel]');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (48, 3, 'name', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (102, 48, 'Advanced Beach Racket Set Jawista Pro [خشب طبيعي]');

-- ── collection ───────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (49, 3, 'collection', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (103, 49, 'Collection 01 El Mediterraneo');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (50, 3, 'collection', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (104, 50, 'Collection 01 El Mediterraneo');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (51, 3, 'collection', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (105, 51, 'Collection 01 El Mediterraneo');

-- ── description ──────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (52, 3, 'description', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (106, 52, 'Designed for more control, stronger impact, and a refined playing experience under the sun.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (107, 52, 'The Jawista Pro set features two rackets crafted from marine plywood, engineered for durability, precision, and improved inertia.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (108, 52, 'The refined wood processing enhances balance and responsiveness, allowing for cleaner shots and more controlled exchanges.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (109, 52, 'Built for players ready to elevate their game, each detail is designed to deliver a more consistent and dynamic feel.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (110, 52, 'Paired with two Jawista balls, this set supports longer rallies, sharper control, and a more intentional way of playing, whether you''re improving your technique or playing at a higher level.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (111, 52, 'Balanced, resistant, and performance-driven, it''s the natural next step after your first rallies.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (112, 52, 'A step forward in the game. A deeper way to play.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (53, 3, 'description', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (113, 53, 'Pensé pour plus de contrôle, d''impact et une expérience de jeu plus maîtrisée sous le soleil.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (114, 53, 'Le set Jawista Pro accompagne une pratique plus engagée, où chaque geste gagne en précision et en intention.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (115, 53, 'Conçues en contreplaqué marin, les raquettes offrent une résistance accrue et une meilleure inertie, pour des frappes plus nettes et des échanges plus contrôlés.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (116, 53, 'Le travail du bois, affiné dans les détails, améliore l''équilibre et la réactivité, permettant un jeu plus fluide, plus précis.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (117, 53, 'Associé à deux balles Jawista, ce set favorise des échanges plus longs, un meilleur contrôle, et une manière de jouer plus consciente, que ce soit pour affiner sa technique ou évoluer vers un niveau plus avancé.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (118, 53, 'Équilibré, résistant et orienté performance, il s''inscrit naturellement comme la suite après les premiers échanges.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (119, 53, 'Une progression dans le jeu. Une manière plus profonde de jouer.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (54, 3, 'description', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (120, 54, 'مصمّمة لمزيد من التحكّم، وقوّة الضربة، وتجربة لعب أكثر دقّة تحت الشمس.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (121, 54, 'ترافق مجموعة Jawista Pro مستوى أكثر تقدّمًا، حيث يكتسب كلّ تحرّك دقّةً ووعيًا أكبر.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (122, 54, 'مصنوعة من الخشب الرقائقي البحري، توفّر المضارب مقاومة أعلى وعزمًا أفضل، ما يمنح ضربات أنقى وتبادلات أكثر تحكّمًا.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (123, 54, 'كما يساهم العمل الدقيق على الخشب في تحسين التوازن وسرعة الاستجابة، لأسلوب لعب أكثر سلاسة ودقّة.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (124, 54, 'ومع كرتين من Jawista، تدعم هذه المجموعة تبادلات أطول، وتحكّمًا أدق، وطريقة لعب أكثر وعيًا، سواء لتطوير المهارات أو للانتقال إلى مستوى أعلى.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (125, 54, 'متوازنة، متينة وموجّهة للأداء، تشكّل الخطوة الطبيعية بعد البدايات الأولى.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (126, 54, 'تقدّم في اللعب. وعمق أكبر في التجربة.');

-- ── badge ────────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (55, 3, 'badge', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (127, 55, 'Locally made | Limited drop.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (56, 3, 'badge', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (128, 56, 'Conçu et fabriqué localement | édition limitée.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (57, 3, 'badge', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (129, 57, 'مصمّمة ومصنوعة محليًا | إصدار محدود.');

-- ── material ─────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (58, 3, 'material', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (130, 58, 'Marine Plywood');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (59, 3, 'material', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (131, 59, 'Raquettes en contreplaqué marin léger');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (60, 3, 'material', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (132, 60, 'مضارب من الخشب الرقائقي البحري');

-- ── dimensions ───────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (61, 3, 'dimensions', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (133, 61, '38 × 23.5 cm');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (62, 3, 'dimensions', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (134, 62, '38 × 23,5 cm');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (63, 3, 'dimensions', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (135, 63, '38 × 23.5 سم');

-- ── product_details ──────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (64, 3, 'product_details', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (136, 64, 'Precision wood selection & racket processing');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (137, 64, 'Set of 2 Rackets');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (138, 64, 'Soft Rubber Grip Handles for enhanced control');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (139, 64, 'Includes 2 Jawista Balls');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (140, 64, 'Locally designed & produced in Tunisia');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (65, 3, 'product_details', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (141, 65, 'Sélection et travail du bois optimisés pour la précision');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (142, 65, 'Set de 2 raquettes');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (143, 65, 'Poignées en caoutchouc souple pour un contrôle renforcé');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (144, 65, '2 balles Jawista incluses');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (66, 3, 'product_details', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (145, 66, 'مجموعة مكوّنة من مضربين');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (146, 66, 'مقابض مطاطية ناعمة');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (147, 66, 'تتضمن كرتين من Jawista');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (148, 66, 'تصميم وإنتاج محلي في تونس');


-- ─────────────────────────────────────────────────────────────────────
-- PRODUCT 4: Jawista Ball Set [Black]
-- SKU: JAW-BALL-SET-BLACK
-- ─────────────────────────────────────────────────────────────────────

-- ── name ─────────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (67, 4, 'name', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (149, 67, 'Jawista Ball Set [Black]');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (68, 4, 'name', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (150, 68, 'Jawista Ball Set [Noir]');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (69, 4, 'name', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (151, 69, 'Jawista Ball Set [أسود]');

-- ── collection ───────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (70, 4, 'collection', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (152, 70, 'Collection 01 El Mediterraneo');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (71, 4, 'collection', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (153, 71, 'Collection 01 El Mediterraneo');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (72, 4, 'collection', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (154, 72, 'Collection 01 El Mediterraneo');

-- ── description ──────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (73, 4, 'description', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (155, 73, 'Small essentials. Maximum play.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (156, 73, 'The Jawista Ball Set brings together 3 beach tennis balls designed for smooth rallies, consistent bounce, and effortless play on sand.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (157, 73, 'Lightweight and durable, they''re made to keep the game flowing whether you''re practicing, playing casually, or pushing longer exchanges.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (158, 73, 'Each pack comes in a compact, easy-carry mini bag, perfect to keep your balls together, protected, and always ready for the next match.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (159, 73, 'Simple, practical, and made for everyday play.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (74, 4, 'description', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (160, 74, 'Des essentiels discrets. Un jeu sans interruption.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (161, 74, 'Le Jawista Ball Set réunit trois balles de beach tennis conçues pour des échanges fluides, un rebond constant et un jeu naturel sur le sable.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (162, 74, 'Légères et résistantes, elles accompagnent chaque rythme, entraînement, moments informels ou échanges prolongés.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (163, 74, 'Pensées pour maintenir la continuité du jeu, sans effort.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (164, 74, 'Chaque set est accompagné d''un mini sac compact, facile à transporter, pour garder vos balles protégées, organisées, et toujours prêtes pour la prochaine partie.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (165, 74, 'Simple, fonctionnel, essentiel au quotidien.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (75, 4, 'description', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (166, 75, 'أساسيات هادئة. ولعب بلا انقطاع.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (167, 75, 'تجمع مجموعة Jawista ثلاث كرات شاطئية مصمّمة لتبادلات سلسة، وارتداد متوازن، ولعب طبيعي على الرمل.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (168, 75, 'خفيفة ومتينة، ترافق مختلف الإيقاعات، من التمرين إلى اللحظات العفوية أو التبادلات الأطول.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (169, 75, 'مصمّمة للحفاظ على استمرارية اللعب، دون جهد.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (170, 75, 'تأتي كل مجموعة مع حقيبة صغيرة مدمجة وسهلة الحمل، لحفظ الكرات، تنظيمها، وإبقائها جاهزة دائمًا للمباراة القادمة.');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (171, 75, 'بسيطة، عملية، وأساسية في كل يوم.');

-- ── badge ────────────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (76, 4, 'badge', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (172, 76, 'Locally made.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (77, 4, 'badge', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (173, 77, 'Conçu et fabriqué localement.');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (78, 4, 'badge', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (174, 78, 'مصمّمة ومصنوعة محليًا.');

-- ── dimensions ───────────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (79, 4, 'dimensions', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (175, 79, '4 mm');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (80, 4, 'dimensions', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (176, 80, '4 mm');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (81, 4, 'dimensions', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (177, 81, '4 مم');

-- ── product_details ──────────────────────────────────────────────────

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (82, 4, 'product_details', 'en');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (178, 82, '3 Jawista Beach Tennis Balls');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (179, 82, 'Consistent bounce for smooth play');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (180, 82, 'Lightweight & durable design');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (181, 82, 'Printed Jawista logo on each ball');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (182, 82, 'Includes carry bag');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (183, 82, 'Locally designed & produced in Tunisia');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (83, 4, 'product_details', 'fr');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (184, 83, 'Set de 3 balles Jawista');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (185, 83, 'Rebond régulier pour des échanges fluides');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (186, 83, 'Conception légère et résistante');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (187, 83, 'Logo Jawista imprimé sur chaque balle');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (188, 83, 'Mini sac de transport inclus');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (189, 83, 'Conçu et produit localement en Tunisie');

INSERT INTO product_attributes (id, product_id, attribute_key, language) VALUES (84, 4, 'product_details', 'ar');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (190, 84, 'مجموعة من 3 كرات Jawista للبيتش تنس');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (191, 84, 'ارتداد منتظم لتبادلات سلسة');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (192, 84, 'تصميم خفيف ومتين');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (193, 84, 'شعار Jawista مطبوع على كل كرة');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (194, 84, 'حقيبة حمل صغيرة مرفقة');
INSERT INTO attribute_values (id, product_attribute_id, value) VALUES (195, 84, 'تصميم وإنتاج محلي في تونس');


-- =====================================================================
-- 7. ATTRIBUTE KEY TRANSLATIONS
-- =====================================================================

INSERT INTO attribute_key_translations (id, attribute_key, language, label)
VALUES
(1,  'name',                 'en', 'Name'),
(2,  'name',                 'fr', 'Nom'),
(3,  'name',                 'ar', 'الاسم'),
(4,  'collection',           'en', 'Collection'),
(5,  'collection',           'fr', 'Collection'),
(6,  'collection',           'ar', 'المجموعة'),
(7,  'description',          'en', 'Description'),
(8,  'description',          'fr', 'Description'),
(9,  'description',          'ar', 'الوصف'),
(10, 'badge',                'en', 'Badge'),
(11, 'badge',                'fr', 'Badge'),
(12, 'badge',                'ar', 'الشارة'),
(13, 'material',             'en', 'Material'),
(14, 'material',             'fr', 'Matière'),
(15, 'material',             'ar', 'الخامة'),
(16, 'dimensions',           'en', 'Dimensions'),
(17, 'dimensions',           'fr', 'Dimensions'),
(18, 'dimensions',           'ar', 'الأبعاد'),
(19, 'product_details',      'en', 'Product Details'),
(20, 'product_details',      'fr', 'Détails du produit'),
(21, 'product_details',      'ar', 'تفاصيل المنتج'),
(22, 'washing_instructions', 'en', 'Washing'),
(23, 'washing_instructions', 'fr', 'Lavage'),
(24, 'washing_instructions', 'ar', 'الغسل'),
(25, 'drying_instructions',  'en', 'Drying'),
(26, 'drying_instructions',  'fr', 'Séchage'),
(27, 'drying_instructions',  'ar', 'التجفيف');


-- =====================================================================
-- RESET SEQUENCES (PostgreSQL)
-- =====================================================================

SELECT setval('products_id_seq',                   (SELECT MAX(id) FROM products));
SELECT setval('product_variants_id_seq',           (SELECT MAX(id) FROM product_variants));
SELECT setval('product_pricing_id_seq',            (SELECT MAX(id) FROM product_pricing));
SELECT setval('inventory_id_seq',                  (SELECT MAX(id) FROM inventory));
SELECT setval('product_attributes_id_seq',         (SELECT MAX(id) FROM product_attributes));
SELECT setval('attribute_values_id_seq',           (SELECT MAX(id) FROM attribute_values));
SELECT setval('attribute_key_translations_id_seq', (SELECT MAX(id) FROM attribute_key_translations));


-- =====================================================================
-- SUMMARY
-- =====================================================================
-- products:                    4 rows
-- product_variants:            5 rows  (T-shirt S/M/L/XL/XXL)
-- product_pricing:             4 rows
-- inventory:                   8 rows  (5 variant + 3 product)
-- product_attributes:         84 rows
-- attribute_values:          195 rows
-- attribute_key_translations: 27 rows
-- product_assets:              0 rows  (to be populated later)
-- TOTAL:                     323 rows
-- =====================================================================