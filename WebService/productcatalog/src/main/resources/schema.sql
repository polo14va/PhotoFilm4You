 INSERT INTO Category (name, description, parent_id)
VALUES ('Cámaras', 'Cámaras', null)
     , ('Cámaras de Fotos', 'Cámaras de Fotos', 1)
     , ('Cámaras de Video', 'Cámaras de Video', 1)
     , ('Iluminación', 'Todo tipo de accesorios de iluminación', null)
     , ('Focos', 'Focos direccionales, omnidireccionales, etc.', 4)
     , ('Difusores', 'Difusores para focos', 4)
;

INSERT INTO product (name, description, daily_price, brand, model, category_id)
VALUES ('Canon 500D', 'Cámara de fotos Canon 500D', 100, 'Canon', '500D', 2)
     , ('Canon EOS R8', 'Cámara de fotos Canon EOS R8', 200, 'Canon', 'EOS R8', 2)
     , ('Canon EOS R5 C', 'Cámara de video Canon EOS R5 C', 250, 'Canon', 'EOS R5 C', 3)
     , ('Foco Phillips 120L', 'Foco LED Phillips de luz blanca 120W. Sin difusor', 80, 'Phillips', '120L', 5)
     , ('Foco Phillips 220L', 'Foco LED Phillips de luz blanca 220W. Sin difusor', 120, 'Phillips', '220L', 5)
     , ('Canon Rebel T7', 'Entry-level DSLR with 24.2MP sensor and touchscreen', 120, 'Canon', 'Rebel T7i', 2),
       ('Fujifilm X-T4', 'Mirrorless camera with 26.1MP sensor and 4K 60fps video', 200, 'Fujifilm', 'X-T4', 2),
       ('GoPro Hero 9 Black', 'Action camera with 5K video and HyperSmooth 3.0 stabilization', 300, 'GoPro', 'Hero 9 Black', 1),
       ('Rode VideoMic Pro', 'On-camera shotgun microphone for high-quality audio recording', 150, 'Rode', 'VideoMic Pro', 3),
       ('Sandisk Extreme Pro 128GB SD Card', 'High-performance SD card for fast data transfer', 40, 'Sandisk', 'Extreme Pro 128GB', 3),
       ('Godox AD200Pro Flash', 'Portable and powerful camera flash with 200Ws output', 180, 'Godox', 'AD200Pro', 3),
       ('Tamron 70-200mm f8 Lens', 'Telephoto zoom lens with constant f/2.8 aperture', 600, 'Tamron', '70-200mm f/2.8', 1),
       ('Peak Design Everyday Backpack', 'Versatile and stylish camera backpack for daily use', 120, 'Peak Design', 'Everyday Backpack', 4),
       ('Manfrotto XPRO Ball Head', 'Robust ball head for precise camera positioning', 80, 'Manfrotto', 'XPRO Ball Head', 4),
       ('Joby GorillaPod 5K Kit', 'Flexible and compact tripod for DSLR and mirrorless cameras', 70, 'Joby', 'GorillaPod 5K Kit', 4),
       ('Lowepro ProTactic Camera Bag', 'Professional camera bag with modular accessories', 100, 'Lowepro', 'ProTactic', 4),
       ('Anker PowerCore 26800mAh Power Bank', 'High-capacity power bank for charging camera gear on the go', 50, 'Anker', 'PowerCore 26800', 4),
       ('Neewer 660 LED Video Light', 'Dimmable LED light panel for video and photography', 50, 'Neewer', '660 LED Video Light', 2);

;

INSERT INTO item (serial_number, status, product_id)
VALUES ('11111', 'OPERATIONAL', 1)
    , ('22131', 'OPERATIONAL', 1)
    , ('6552646', 'OPERATIONAL', 1)
    , ('444444', 'OPERATIONAL', 2)
