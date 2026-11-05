-- =====================================================================
-- JAWISTA E-COMMERCE — ASSET POPULATION
-- Populates:
--   1. file_metadata
--   2. asset_links
--   3. product_assets
-- =====================================================================

-- =====================================================================
-- 1. FILE METADATA
-- =====================================================================

INSERT INTO file_metadata (
    id, public_id, original_file_name, stored_file_name, storage_path,
    content_type, file_size, checksum, uploaded_at, uploaded_by
) VALUES
(1,  '6a19a439-c65d-420a-ba65-44c1985492ff', 't-shirt-first-photo.jpeg', 'c8ba03c8-9882-4de1-829b-d1d570aa7156.jpeg', 'product/c8ba03c8-9882-4de1-829b-d1d570aa7156.jpeg', 'image/jpeg', 120107, '21a0dfda2271a895c9d3bf210560e6a1c1e99f01fa5e93f017804dadae42c291', '2026-06-10 15:36:21.826682', 'admin'),
(2,  '0f402466-b932-4854-8e8d-697189fc9877', 'tshirt-second-photo.jpg', '9623f800-487e-40c6-8d5a-c7eff114ee7d.jpg', 'product/9623f800-487e-40c6-8d5a-c7eff114ee7d.jpg', 'image/jpeg', 169993, 'c18ee835d3af77f0ff76ac95882ea549d88b98af4428ea1e10dbdf2214169662', '2026-06-10 15:37:07.331542', 'admin'),
(3,  '8541e00f-d586-453e-8997-c54d87629d7c', 'tshirt-third-photo.jpg', 'bf2f98d2-0469-45a2-b020-be7f94ff3e1c.jpg', 'product/bf2f98d2-0469-45a2-b020-be7f94ff3e1c.jpg', 'image/jpeg', 622257, '4165278957793bc1d892a179f5096b82a08464a9be8bdb239a5002de8a26063e', '2026-06-10 15:37:27.513584', 'admin'),
(4,  'deb46d2d-5a72-48a6-966d-3d3485bbcc71', 'tshirt-forth-photo.jpeg', '8cf03114-58ac-46c1-80c5-12f1235158d5.jpeg', 'product/8cf03114-58ac-46c1-80c5-12f1235158d5.jpeg', 'image/jpeg', 201533, 'd63ccd40b3d62a302eec59d5960d1279439520ee48bc11da0d4a9e2ea44180f0', '2026-06-10 15:37:46.760673', 'admin'),
(5,  '40ee1139-9182-42c1-bfa7-f8fb6a8a8c18', 'tshirt-fifth-photo.jpg', '41b702ab-6e06-49e4-b57f-52140cd4befe.jpg', 'product/41b702ab-6e06-49e4-b57f-52140cd4befe.jpg', 'image/jpeg', 251582, '492a29e1258c2675e82220672097cf46e28fa91bf39b0b16364b9a377498eaf3', '2026-06-10 15:38:06.533333', 'admin'),
(6,  '81a2103d-94dd-4f21-8daf-b38fa707a171', 'tshirt-sixth-photo.jpg', 'a1c3982c-9e05-49bc-9643-f4569c420e02.jpg', 'product/a1c3982c-9e05-49bc-9643-f4569c420e02.jpg', 'image/jpeg', 388442, '3345e05ac187522f042758c011ed936506a9de2e3ec5bb413cc3b1140cbcb3e7', '2026-06-10 15:38:31.137762', 'admin'),
(7,  'f5e21a9f-ac78-465d-90e3-201e59fa872b', 't-shirt-last-photo.jpeg', '48d719b7-6305-4dce-8a0b-b5d2ccc95ca4.jpeg', 'product/48d719b7-6305-4dce-8a0b-b5d2ccc95ca4.jpeg', 'image/jpeg', 144018, '0781c43d582754687a7d8e7027c1c4bc9e984274621416b0323daeaf6ef41c64', '2026-06-10 15:38:42.533344', 'admin'),

(9,  'bc3af6f4-1dcd-4a3c-8a1d-47fc2ec7586e', 'raquet-1-img.jpg', '878e017b-3564-4b0c-9d30-0605833384b6.jpg', 'product/878e017b-3564-4b0c-9d30-0605833384b6.jpg', 'image/jpeg', 912647, 'd55a8db357d9ba5499459ab0e2d2a69fd9032a60d0fc32780b34e974be42fcc6', '2026-06-10 15:40:40.533997', 'admin'),
(10, 'e7601bfa-97c4-456d-aa9c-5ad37bb53dad', 'raquet-2-img.jpg', 'ba8408b0-e5cf-4d27-a438-8a31d3654344.jpg', 'product/ba8408b0-e5cf-4d27-a438-8a31d3654344.jpg', 'image/jpeg', 393833, '6131c6424f02129a753747c7b4f16cae16627fb201c9c9a371a79bab4dea7f63', '2026-06-10 15:40:57.420965', 'admin'),
(11, 'b7ab055c-1d40-41e5-be2f-e505dc3f664c', 'raquet-3-img.jpg', '2af83539-3385-4256-a1f0-33035588f816.jpg', 'product/2af83539-3385-4256-a1f0-33035588f816.jpg', 'image/jpeg', 304573, 'efc936879382f16fb4816204eb379292fbafd004c7107095c1bc7a97565f9e51', '2026-06-10 15:41:06.851878', 'admin'),
(12, '6e82b9fb-b4d2-4a1c-8353-7efcd6aa8964', 'raquet-4-img.jpg', 'e0a5195f-c306-4def-ab19-f023b328f733.jpg', 'product/e0a5195f-c306-4def-ab19-f023b328f733.jpg', 'image/jpeg', 271796, 'f4ae7d18d5ee47507b7b36da63449871267c578c1fea298646e471948135cb0e', '2026-06-10 15:41:17.200917', 'admin'),
(13, 'c429f8b3-fdd6-4f45-a945-b6a9d970c20a', 'raquet-5-img.jpg', '19ae7ce2-a01a-4d19-94eb-eafadfdbb398.jpg', 'product/19ae7ce2-a01a-4d19-94eb-eafadfdbb398.jpg', 'image/jpeg', 279414, '8d7ea253769847fca9084a44078167fa512b11ab1431680a2b7ccb0af8bf1e99', '2026-06-10 15:41:26.628662', 'admin'),
(14, '4b0fadfe-6859-44b7-9eb8-b1ab1a8184fc', 'ball-1.jpeg', 'd7cd1460-67a7-431d-852d-f9e8d97b512c.jpeg', 'product/d7cd1460-67a7-431d-852d-f9e8d97b512c.jpeg', 'image/jpeg', 102944, '9cab15e66b6b2cad550f6133e0c59ee7b291be7188892f887a723d914bb24d90', '2026-06-10 15:50:00.740476', 'admin'),
(15, 'cb401e23-a3cf-4347-9e51-ef653b37ebe9', 'ball-2.jpeg', '5274aeca-ac7e-4b68-aa13-e278fed084c8.jpeg', 'product/5274aeca-ac7e-4b68-aa13-e278fed084c8.jpeg', 'image/jpeg', 110255, '01c31b01503f571b20d66551fae6210e202ae21c7f2264cffd859e846dbb0d80', '2026-06-10 15:50:19.220509', 'admin'),

(17, '859c8d15-4812-49d4-ae25-07f688b90bdf', 'about - 1st ban.jpg', 'a6710d28-262c-4fd7-9bec-4711dde436ff.jpg', 'banner/a6710d28-262c-4fd7-9bec-4711dde436ff.jpg', 'image/jpeg', 318915, '003d3032b0464b3788882dd2426a063fece1e7a971cc3e293e1be9a1ae2f4ddc', '2026-06-11 23:22:54.070823', 'admin'),
(18, '5ec25494-e767-4196-9f95-dfb946d73533', 'about- 2nd banner.jpg', 'bab4c0d6-7f43-4a2a-b46f-d301e7811219.jpg', 'banner/bab4c0d6-7f43-4a2a-b46f-d301e7811219.jpg', 'image/jpeg', 277221, '34d04acc9ad54de8c0216cc9bab982e4ba932c5966bdead7ec0b6c94f83c1f2e', '2026-06-11 23:23:33.445602', 'admin'),
(20, '59dcad9b-354f-4066-8b78-b50a43c715c1', 'ABout us  (1).jpg', 'd92a8093-853c-422e-b3d4-151ba914f820.jpg', 'banner/d92a8093-853c-422e-b3d4-151ba914f820.jpg', 'image/jpeg', 131682, '1f1a45628e6544e023e0ae811a52628fc0ca75a04807212818c178415086a569', '2026-06-11 23:26:59.325943', 'admin'),
(21, '9f47b078-6b90-43a4-9275-62bcc35fc849', 'ABout us  (2).jpg', '0663301a-157d-4241-bfb5-30bd2313b550.jpg', 'banner/0663301a-157d-4241-bfb5-30bd2313b550.jpg', 'image/jpeg', 77928, 'cd6899f125da512219995f22283534bf61a923fb6dab3c6e1b6fb930b28911c6', '2026-06-11 23:27:18.759764', 'admin'),
(22, '264c2fa1-5373-4a4b-b242-63c1c6f63f34', 'ABout us  (5).jpg', 'ce3040c4-7bc8-4cde-a31a-2b9bef3984ea.jpg', 'banner/ce3040c4-7bc8-4cde-a31a-2b9bef3984ea.jpg', 'image/jpeg', 155546, 'e7a6c6f1d7b4f11a37778caafb1cfe6c67d27580dcff83c4141540cc29c507e4', '2026-06-11 23:27:46.987584', 'admin'),
(23, 'c99ba72e-8bd9-4f56-8cfb-364eb5e44e67', 'ABout us  (6).jpg', '6d084527-5680-4705-b3c0-70dfdaf84d14.jpg', 'banner/6d084527-5680-4705-b3c0-70dfdaf84d14.jpg', 'image/jpeg', 161698, '0bc30c543e54d1c31248e3954e45cd9ed981d7d5cff235b01bb915ed006124f7', '2026-06-11 23:28:09.706282', 'admin'),
(24, '87357e61-b29c-443f-a4e6-c10bad85a879', 'ABout us  (3).jpg', 'd40d5828-962c-402d-8206-f64518d7180c.jpg', 'banner/d40d5828-962c-402d-8206-f64518d7180c.jpg', 'image/jpeg', 91941, '596fb224b8e4a827f01137c08e1ae78401f7810f9ec2a5a5bf06d109d08bdf31', '2026-06-11 23:29:46.133985', 'admin'),

(25, '8a62bcc7-8df8-44f4-b4b9-8416803e1965', 'watermarked_img_11158836722272154819.png', '04ccdcf4-b0b6-4b54-b687-20343d7a21be.png', 'product/04ccdcf4-b0b6-4b54-b687-20343d7a21be.png', 'image/png', 733886, 'b9e59dfe370471fa0cd688c21bfd9cabec45510387bd9f2873f76f2c0d055dd8', '2026-06-11 23:35:43.337446', 'admin'),
(26, 'badffc7d-0819-4467-ab49-c801bc8ddf14', 'fd156e47-e100-45a5-8126-91814d247fcc.jpg', '61a057e0-ad07-4913-bc2f-2975f9811831.jpg', 'product/61a057e0-ad07-4913-bc2f-2975f9811831.jpg', 'image/jpeg', 109119, 'e2f7d63d3cc81ac27d8ff9beb405736d7ce3bd4c1d686036ec2c17b46585937b', '2026-06-11 23:36:00.753399', 'admin'),
(27, 'b91ad521-c3e9-4828-9d67-eb3aaa9ee1cc', '9b233983-db11-4932-9d66-aecc24f1bfa3.jpg', 'ce518465-f477-4595-9b30-ee8179b6e8b2.jpg', 'product/ce518465-f477-4595-9b30-ee8179b6e8b2.jpg', 'image/jpeg', 128115, '974ba737040b7be9549620f0e5d094c6749e47946927732f1baf485ddd20515b', '2026-06-11 23:36:10.280512', 'admin'),
(28, 'f445155e-17b6-4078-b274-c09f27463a04', '442529e1-e129-41cf-8ad1-a78b1d538e4a.jpg', '643a6dd3-1483-4254-a63e-e121a20e4cac.jpg', 'product/643a6dd3-1483-4254-a63e-e121a20e4cac.jpg', 'image/jpeg', 106500, 'c8d74513aca56c4245750b34f18004b370803da80427de0ef2fb998a557c172f', '2026-06-11 23:36:19.222272', 'admin'),

(29, 'd914a8ec-c97b-4fba-82e6-5bda29f641c4', 'Banner home2.jpg', '80d35c37-8209-4d57-b5be-fa5afd4abaaf.jpg', 'hero/80d35c37-8209-4d57-b5be-fa5afd4abaaf.jpg', 'image/jpeg', 559123, '9ae57b6a95f813d71af9480d41c2d2f2559790c0543f67f3a69ceb07c13ed216', '2026-06-11 23:42:30.607956', 'admin'),
(30, 'e4dc169d-21ee-4018-8520-14d8d3e09102', 'Banner home1.jpg', '961ec254-9aa9-45a5-8a22-c23aa98d29af.jpg', 'hero/961ec254-9aa9-45a5-8a22-c23aa98d29af.jpg', 'image/jpeg', 502227, 'fa7b37cab4f9475b23d868586d04c1afd22cc48af53786fd788d3952d92bcfa3', '2026-06-11 23:42:42.807990', 'admin'),
(31, '5d0c77ac-6b34-405d-9057-c7b27e57f2b7', 'Banner home.jpg', '31ebd908-8aca-4c7b-9035-59b40ddb1952.jpg', 'hero/31ebd908-8aca-4c7b-9035-59b40ddb1952.jpg', 'image/jpeg', 2253331, '1620ee5f394e2de93754b60897862bc1c5c12b16566e0112efacf472a96b5239', '2026-06-11 23:42:55.941530', 'admin'),

(32, 'b82a1ac9-418b-4c5b-bb80-55037f83464a', 'hero_01.jpg', 'e329d42d-5a1e-4b43-a211-06c32557c6a3.jpg', 'banner/e329d42d-5a1e-4b43-a211-06c32557c6a3.jpg', 'image/jpeg', 1057296, 'c3b95ec3d1d56b5d72fa19bd4dd55a3bbfbdce1eb715da29ac493d870238a080', '2026-06-11 23:47:11.272190', 'admin'),
(33, '04f91296-3b99-4c08-a46c-c3deab6ff9f6', 'hero_02.jpg', 'e0624b85-3e0f-4696-b185-5483c92c3328.jpg', 'banner/e0624b85-3e0f-4696-b185-5483c92c3328.jpg', 'image/jpeg', 353956, 'eba242f4398c078b3b077dd40228837630b8e0ea7074e2640b8a9ddf4e3746f2', '2026-06-11 23:47:25.005278', 'admin'),
(34, '58fd3cd7-47d7-41d4-b726-2ee85597e013', 'hero_03.jpg', 'a66011f2-ee17-4426-97b5-684d676f8520.jpg', 'banner/a66011f2-ee17-4426-97b5-684d676f8520.jpg', 'image/jpeg', 468164, '65f7be0f1bba364707cc38035d32accfa6f91abd709fe4d6476b767184e3356a', '2026-06-11 23:47:37.643548', 'admin'),
(35, '4c144e0d-8dd4-4736-bf9d-057a5754e43c', 'Privacy.jpg', '40f6455b-2b34-457d-9061-f039b19f900a.jpg', 'banner/40f6455b-2b34-457d-9061-f039b19f900a.jpg', 'image/jpeg', 118249, 'ef5e1687a0a3a32527fab622e8b7b55d320fcd7b37711b8b2537249cc9eb1e77', '2026-06-12 02:15:47.315238', NULL),
(36, 'f9367f0d-058d-4c48-943d-c1cddb9b74d7', 'subscription-mail-header.png', '00c5c2db-20e3-49a1-b1c6-4c730599b6cd.png', 'brand/00c5c2db-20e3-49a1-b1c6-4c730599b6cd.png', 'image/png', 105716, '4688556a5a0f07c9fb3575022a17a33f35114bdca3ebd13d9978760d13cf4af5', '2026-06-12 03:19:33.082223', NULL),
(37, '3d015a60-4dc0-4635-96f4-bc419c141ceb', 'jawista-logo-light.png', 'c9624c02-60d7-4908-a67f-7ab41c321047.png', 'brand/c9624c02-60d7-4908-a67f-7ab41c321047.png', 'image/png', 241565, 'e19e73f0bd0032add8ef709c4527763e10892ed2272502479d49636959e81af6', '2026-06-12 03:20:14.888980', NULL),
(38, '2d51cf22-989a-498f-86b1-61a52cd75e3b', 'jawista-logo-dark.png', 'f8c7b444-fbd0-4a03-aa70-6ff28a3ba607.png', 'brand/f8c7b444-fbd0-4a03-aa70-6ff28a3ba607.png', 'image/png', 255825, '6a36c3cbaa524ef1042c412dce38af8c7a1f74a81c146218d156c1cbf190e966', '2026-06-12 03:22:38.512134', NULL),
(39, '65bad646-e3f2-42d0-95da-65b2e077d180', 'Artboard 3.jpg', '1f5dbe1c-6027-4c89-a27f-35810222abc3.jpg', 'hero_mobile/1f5dbe1c-6027-4c89-a27f-35810222abc3.jpg', 'image/jpeg', 244536, 'd422789980c693ee49f4826d6343ac8e7961a1748826afc36e16c13f34807e4a', '2026-06-12 03:54:07.706112', NULL),
(40, 'b07088a8-7700-4b1c-8bd7-bb22303331f5', 'Artboard 2.jpg', '66d08218-e567-4b28-b2c4-e6486a4c659c.jpg', 'hero_mobile/66d08218-e567-4b28-b2c4-e6486a4c659c.jpg', 'image/jpeg', 242124, 'de86aa8a4e697bb39aeea5ca75c3ea7041f0c1dd49fab70d94842f15d86571eb', '2026-06-12 03:54:22.037289', NULL),
(41, '2a2019fa-d64e-44df-8e15-f44d7b59e0f6', 'Artboard 1.jpg', 'c0403a04-8325-4a42-aa5a-a7b65385b9ce.jpg', 'hero_mobile/c0403a04-8325-4a42-aa5a-a7b65385b9ce.jpg', 'image/jpeg', 552069, '62fde7ebc53a81026033d881fe92b8ea9410da75da542a47608ef71eb5ee2fac', '2026-06-12 03:54:49.175000', NULL);

-- =====================================================================
-- 2. ASSET LINKS
-- =====================================================================

INSERT INTO asset_links (
    id, file_id, owner_type, owner_id, purpose, alt_text, display_order, is_primary
) VALUES
(1, 1, 'PRODUCT', 1, 'GALLERY', 'Front view', 0, true),
(2, 2, 'PRODUCT', 1, 'GALLERY', 'Front view', 1, false),
(3, 3, 'PRODUCT', 1, 'GALLERY', 'Front view', 2, false),
(4, 4, 'PRODUCT', 1, 'GALLERY', 'Front view', 3, false),
(5, 5, 'PRODUCT', 1, 'GALLERY', 'Front view', 4, false),
(6, 6, 'PRODUCT', 1, 'GALLERY', 'Front view', 5, false),
(7, 7, 'PRODUCT', 1, 'GALLERY', 'Front view', 6, false),

(9, 9, 'PRODUCT', 3, 'GALLERY', 'Front view', 1, true),
(10, 10, 'PRODUCT', 3, 'GALLERY', 'Front view', 2, false),
(11, 11, 'PRODUCT', 3, 'GALLERY', 'Front view', 3, false),
(12, 12, 'PRODUCT', 3, 'GALLERY', 'Front view', 4, false),
(13, 13, 'PRODUCT', 3, 'GALLERY', 'Front view', 5, false),

(14, 14, 'PRODUCT', 4, 'GALLERY', 'Front view', 0, true),
(15, 15, 'PRODUCT', 4, 'GALLERY', 'Front view', 1, false),

(17, 17, 'BANNER', 41, 'GALLERY', 'Hero slide 1', 0, true),
(18, 18, 'BANNER', 70, 'GALLERY', 'Hero slide 1', 0, true),
(20, 20, 'BANNER', 71, 'GALLERY', 'Hero slide 1', 0, true),
(21, 21, 'BANNER', 72, 'GALLERY', 'Hero slide 1', 0, true),
(22, 22, 'BANNER', 73, 'GALLERY', 'Hero slide 1', 0, true),
(23, 23, 'BANNER', 74, 'GALLERY', 'Hero slide 1', 0, true),
(24, 24, 'BANNER', 75, 'GALLERY', 'Hero slide 1', 0, true),

(25, 25, 'PRODUCT', 2, 'GALLERY', 'Front view', 0, true),
(26, 26, 'PRODUCT', 2, 'GALLERY', 'Front view', 1, false),
(27, 27, 'PRODUCT', 2, 'GALLERY', 'Front view', 2, false),
(28, 28, 'PRODUCT', 2, 'GALLERY', 'Front view', 3, false),

(29, 29, 'HERO', 1, 'GALLERY', 'Hero slide 1', 0, true),
(30, 30, 'HERO', 2, 'GALLERY', 'Hero slide 1', 0, true),
(31, 31, 'HERO', 3, 'GALLERY', 'Hero slide 1', 0, true),

(32, 32, 'BANNER', 30, 'GALLERY', 'Hero slide 1', 0, true),
(33, 33, 'BANNER', 31, 'GALLERY', 'Hero slide 1', 0, true),
(34, 34, 'BANNER', 32, 'GALLERY', 'Hero slide 1', 0, true),
(35, 35, 'BANNER', 110, 'GALLERY', 'Legal page showcase image', 0, true),

(36, 36, 'BRAND', 11, 'GALLERY', 'Welcome email header logo', 0, true),
(37, 37, 'BRAND', 12, 'GALLERY', 'Email footer logo', 0, true),
(38, 38, 'BRAND', 10, 'GALLERY', 'Order confirmation header logo', 0, true),

(39, 39, 'HERO_MOBILE', 1, 'GALLERY', 'Hero slide 1 mobile', 0, true),
(40, 40, 'HERO_MOBILE', 2, 'GALLERY', 'Hero slide 1 mobile', 0, true),
(41, 41, 'HERO_MOBILE', 3, 'GALLERY', 'Hero slide 1 mobile', 0, true);

-- =====================================================================
-- 3. PRODUCT ASSETS
-- IMPORTANT: use relative /media URLs, never localhost absolute URLs
-- =====================================================================

INSERT INTO product_assets (
    id, product_id, asset_type, url, alt_text, display_order, is_primary, asset_link_id
) VALUES
(1, 1, 'image/jpeg', '/media/product/c8ba03c8-9882-4de1-829b-d1d570aa7156.jpeg', 'Front view', 0, true, 1),
(2, 1, 'image/jpeg', '/media/product/9623f800-487e-40c6-8d5a-c7eff114ee7d.jpg', 'Front view', 1, false, 2),
(3, 1, 'image/jpeg', '/media/product/bf2f98d2-0469-45a2-b020-be7f94ff3e1c.jpg', 'Front view', 2, false, 3),
(4, 1, 'image/jpeg', '/media/product/8cf03114-58ac-46c1-80c5-12f1235158d5.jpeg', 'Front view', 3, false, 4),
(5, 1, 'image/jpeg', '/media/product/41b702ab-6e06-49e4-b57f-52140cd4befe.jpg', 'Front view', 4, false, 5),
(6, 1, 'image/jpeg', '/media/product/a1c3982c-9e05-49bc-9643-f4569c420e02.jpg', 'Front view', 5, false, 6),
(7, 1, 'image/jpeg', '/media/product/48d719b7-6305-4dce-8a0b-b5d2ccc95ca4.jpeg', 'Front view', 6, false, 7),

(9, 3, 'image/jpeg', '/media/product/878e017b-3564-4b0c-9d30-0605833384b6.jpg', 'Front view', 1, true, 9),
(10, 3, 'image/jpeg', '/media/product/ba8408b0-e5cf-4d27-a438-8a31d3654344.jpg', 'Front view', 2, false, 10),
(11, 3, 'image/jpeg', '/media/product/2af83539-3385-4256-a1f0-33035588f816.jpg', 'Front view', 3, false, 11),
(12, 3, 'image/jpeg', '/media/product/e0a5195f-c306-4def-ab19-f023b328f733.jpg', 'Front view', 4, false, 12),
(13, 3, 'image/jpeg', '/media/product/19ae7ce2-a01a-4d19-94eb-eafadfdbb398.jpg', 'Front view', 5, false, 13),

(14, 4, 'image/jpeg', '/media/product/d7cd1460-67a7-431d-852d-f9e8d97b512c.jpeg', 'Front view', 0, true, 14),
(15, 4, 'image/jpeg', '/media/product/5274aeca-ac7e-4b68-aa13-e278fed084c8.jpeg', 'Front view', 1, false, 15),

(16, 2, 'image/png', '/media/product/04ccdcf4-b0b6-4b54-b687-20343d7a21be.png', 'Front view', 0, true, 25),
(17, 2, 'image/jpeg', '/media/product/61a057e0-ad07-4913-bc2f-2975f9811831.jpg', 'Front view', 1, false, 26),
(18, 2, 'image/jpeg', '/media/product/ce518465-f477-4595-9b30-ee8179b6e8b2.jpg', 'Front view', 2, false, 27),
(19, 2, 'image/jpeg', '/media/product/643a6dd3-1483-4254-a63e-e121a20e4cac.jpg', 'Front view', 3, false, 28);

-- =====================================================================
-- RESET SEQUENCES
-- =====================================================================

SELECT setval('file_metadata_id_seq', (SELECT COALESCE(MAX(id), 1) FROM file_metadata));
SELECT setval('asset_links_id_seq', (SELECT COALESCE(MAX(id), 1) FROM asset_links));
SELECT setval('product_assets_id_seq', (SELECT COALESCE(MAX(id), 1) FROM product_assets));