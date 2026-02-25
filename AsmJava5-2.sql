USE AsmJava5;
GO

-- ========== 1) ACCOUNTS ==========
-- DB AsmJava5 có thêm Address, Phone nên insert nên khai báo rõ cột
INSERT INTO Accounts (Username, Password, Fullname, Email, Photo, Address, Phone, Activated, Admin) VALUES
('user01', '123', N'Nguyễn Văn A', N'user01@gmail.com', N'avatars/user01.png', N'Quận 1, TP.HCM', '0900000001', 1, 0),
('user02', '123', N'Trần Thị B', N'user02@gmail.com', N'avatars/user02.webp', N'Quận 3, TP.HCM', '0900000002', 1, 0),
('admin',  '123', N'Quản trị viên', N'admin@gmail.com',  N'avatars/admin.jpg',  N'TP.HCM',         '0900000000', 1, 1);
GO

-- ========== 2) CATEGORIES ==========
INSERT INTO Categories (Id, Name) VALUES
('HB', N'Hoa bó'),
('HC', N'Hoa cưới'),
('GH', N'Giỏ hoa'),
('HH', N'Hộp hoa'),
('BH', N'Bình hoa'),
('KM', N'Kệ hoa chúc mừng'),
('KB', N'Kệ hoa chia buồn'),
('TC', N'Giỏ hoa trái cây'),
('DB', N'Hoa để bàn');
GO

-- ========== 3) PRODUCTS ==========
-- Luôn chỉ định cột để tránh lỗi
INSERT INTO Products (Name, Image, Price, CreateDate, Available, CategoryId, Amount) VALUES
-- Hoa bó (25)
(N'Hoa bó 01', 'products/hoabo/hoabo_01.jpg', 350000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 02', 'products/hoabo/hoabo_02.jpg', 360000, GETDATE(), 1, 'HB', 10),
(N'Hoa bó 03', 'products/hoabo/hoabo_03.jpg', 370000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 04', 'products/hoabo/hoabo_04.jpg', 380000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 05', 'products/hoabo/hoabo_05.jpg', 390000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 06', 'products/hoabo/hoabo_06.jpg', 400000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 07', 'products/hoabo/hoabo_07.jpg', 410000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 08', 'products/hoabo/hoabo_08.jpg', 420000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 09', 'products/hoabo/hoabo_09.jpg', 430000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 10', 'products/hoabo/hoabo_10.jpg', 440000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 11', 'products/hoabo/hoabo_11.jpg', 450000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 12', 'products/hoabo/hoabo_12.jpg', 460000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 13', 'products/hoabo/hoabo_13.jpg', 470000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 14', 'products/hoabo/hoabo_14.jpg', 480000, GETDATE(), 1, 'HB', 10),
(N'Hoa bó 15', 'products/hoabo/hoabo_15.jpg', 490000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 16', 'products/hoabo/hoabo_16.jpg', 500000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 17', 'products/hoabo/hoabo_17.jpg', 510000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 18', 'products/hoabo/hoabo_18.jpg', 520000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 19', 'products/hoabo/hoabo_19.jpg', 530000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 20', 'products/hoabo/hoabo_20.jpg', 540000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 21', 'products/hoabo/hoabo_21.jpg', 550000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 22', 'products/hoabo/hoabo_22.jpg', 560000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 23', 'products/hoabo/hoabo_23.jpg', 570000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 24', 'products/hoabo/hoabo_24.jpg', 580000, GETDATE(), 1, 'HB', 20),
(N'Hoa bó 25', 'products/hoabo/hoabo_25.jpg', 590000, GETDATE(), 1, 'HB', 20),

-- Bình hoa (10)
(N'Bình hoa 01', 'products/binhhoa/binhhoa_01.webp', 650000, GETDATE(), 1, 'BH', 20),
(N'Bình hoa 02', 'products/binhhoa/binhhoa_02.webp', 660000, GETDATE(), 1, 'BH', 20),
(N'Bình hoa 03', 'products/binhhoa/binhhoa_03.webp', 670000, GETDATE(), 1, 'BH', 20),
(N'Bình hoa 04', 'products/binhhoa/binhhoa_04.webp', 680000, GETDATE(), 1, 'BH', 20),
(N'Bình hoa 05', 'products/binhhoa/binhhoa_05.webp', 690000, GETDATE(), 1, 'BH', 20),
(N'Bình hoa 06', 'products/binhhoa/binhhoa_06.webp', 700000, GETDATE(), 1, 'BH', 20),
(N'Bình hoa 07', 'products/binhhoa/binhhoa_07.webp', 710000, GETDATE(), 1, 'BH', 20),
(N'Bình hoa 08', 'products/binhhoa/binhhoa_08.webp', 720000, GETDATE(), 1, 'BH', 20),
(N'Bình hoa 09', 'products/binhhoa/binhhoa_09.webp', 730000, GETDATE(), 1, 'BH', 20),
(N'Bình hoa 10', 'products/binhhoa/binhhoa_10.webp', 740000, GETDATE(), 1, 'BH', 20),

-- Giỏ hoa (11)
(N'Giỏ hoa 01', 'products/giohoa/giohoa_01.jpg', 550000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 02', 'products/giohoa/giohoa_02.jpg', 560000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 03', 'products/giohoa/giohoa_03.jpg', 570000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 04', 'products/giohoa/giohoa_04.jpg', 580000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 05', 'products/giohoa/giohoa_05.jpg', 590000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 06', 'products/giohoa/giohoa_06.jpg', 600000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 07', 'products/giohoa/giohoa_07.jpg', 610000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 08', 'products/giohoa/giohoa_08.jpg', 620000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 09', 'products/giohoa/giohoa_09.jpg', 630000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 10', 'products/giohoa/giohoa_10.jpg', 640000, GETDATE(), 1, 'GH', 20),
(N'Giỏ hoa 11', 'products/giohoa/giohoa_11.jpg', 650000, GETDATE(), 1, 'GH', 20),

-- Hộp hoa (12)
(N'Hộp hoa 01', 'products/hophoa/hophoa_01.jpg', 600000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 02', 'products/hophoa/hophoa_02.jpg', 610000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 03', 'products/hophoa/hophoa_03.jpg', 620000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 04', 'products/hophoa/hophoa_04.jpg', 630000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 05', 'products/hophoa/hophoa_05.jpg', 640000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 06', 'products/hophoa/hophoa_06.jpg', 650000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 07', 'products/hophoa/hophoa_07.jpg', 660000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 08', 'products/hophoa/hophoa_08.jpg', 670000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 09', 'products/hophoa/hophoa_09.jpg', 680000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 10', 'products/hophoa/hophoa_10.jpg', 690000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 11', 'products/hophoa/hophoa_11.jpg', 700000, GETDATE(), 1, 'HH', 20),
(N'Hộp hoa 12', 'products/hophoa/hophoa_12.jpg', 710000, GETDATE(), 1, 'HH', 20),

-- Hoa cưới (12)
(N'Hoa cưới 01', 'products/hoacuoi/hoacuoi_01.jpg', 1200000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 02', 'products/hoacuoi/hoacuoi_02.jpg', 1250000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 03', 'products/hoacuoi/hoacuoi_03.jpg', 1300000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 04', 'products/hoacuoi/hoacuoi_04.jpg', 1350000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 05', 'products/hoacuoi/hoacuoi_05.jpg', 1400000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 06', 'products/hoacuoi/hoacuoi_06.jpg', 1450000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 07', 'products/hoacuoi/hoacuoi_07.jpg', 1500000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 08', 'products/hoacuoi/hoacuoi_08.jpg', 1550000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 09', 'products/hoacuoi/hoacuoi_09.jpeg', 1600000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 10', 'products/hoacuoi/hoacuoi_10.jpeg', 1650000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 11', 'products/hoacuoi/hoacuoi_11.jpeg', 1700000, GETDATE(), 1, 'HC', 20),
(N'Hoa cưới 12', 'products/hoacuoi/hoacuoi_12.jpg', 1750000, GETDATE(), 1, 'HC', 20),

-- Kệ chúc mừng (10)
(N'Kệ chúc mừng 01', 'products/kechucmung/kechucmung_01.webp', 1500000, GETDATE(), 1, 'KM', 20),
(N'Kệ chúc mừng 02', 'products/kechucmung/kechucmung_02.webp', 1550000, GETDATE(), 1, 'KM', 20),
(N'Kệ chúc mừng 03', 'products/kechucmung/kechucmung_03.webp', 1600000, GETDATE(), 1, 'KM', 20),
(N'Kệ chúc mừng 04', 'products/kechucmung/kechucmung_04.webp', 1650000, GETDATE(), 1, 'KM', 20),
(N'Kệ chúc mừng 05', 'products/kechucmung/kechucmung_05.webp', 1700000, GETDATE(), 1, 'KM', 20),
(N'Kệ chúc mừng 06', 'products/kechucmung/kechucmung_06.webp', 1750000, GETDATE(), 1, 'KM', 20),
(N'Kệ chúc mừng 07', 'products/kechucmung/kechucmung_07.webp', 1800000, GETDATE(), 1, 'KM', 20),
(N'Kệ chúc mừng 08', 'products/kechucmung/kechucmung_08.webp', 1850000, GETDATE(), 1, 'KM', 20),
(N'Kệ chúc mừng 09', 'products/kechucmung/kechucmung_09.webp', 1900000, GETDATE(), 1, 'KM', 20),
(N'Kệ chúc mừng 10', 'products/kechucmung/kechucmung_10.webp', 1950000, GETDATE(), 1, 'KM', 20),

-- Kệ chia buồn (10)
(N'Kệ chia buồn 01', 'products/kechiabuon/kechiabuon_01.webp', 1400000, GETDATE(), 1, 'KB', 20),
(N'Kệ chia buồn 02', 'products/kechiabuon/kechiabuon_02.webp', 1450000, GETDATE(), 1, 'KB', 20),
(N'Kệ chia buồn 03', 'products/kechiabuon/kechiabuon_03.webp', 1500000, GETDATE(), 1, 'KB', 20),
(N'Kệ chia buồn 04', 'products/kechiabuon/kechiabuon_04.webp', 1550000, GETDATE(), 1, 'KB', 20),
(N'Kệ chia buồn 05', 'products/kechiabuon/kechiabuon_05.webp', 1600000, GETDATE(), 1, 'KB', 20),
(N'Kệ chia buồn 06', 'products/kechiabuon/kechiabuon_06.webp', 1650000, GETDATE(), 1, 'KB', 20),
(N'Kệ chia buồn 07', 'products/kechiabuon/kechiabuon_07.webp', 1700000, GETDATE(), 1, 'KB', 20),
(N'Kệ chia buồn 08', 'products/kechiabuon/kechiabuon_08.webp', 1750000, GETDATE(), 1, 'KB', 20),
(N'Kệ chia buồn 09', 'products/kechiabuon/kechiabuon_09.webp', 1800000, GETDATE(), 1, 'KB', 20),
(N'Kệ chia buồn 10', 'products/kechiabuon/kechiabuon_10.webp', 1850000, GETDATE(), 1, 'KB', 20),

-- Giỏ trái cây (5)
(N'Giỏ trái cây 01', 'products/giohoatraicay/giohoatraicay_01.jpg',  900000, GETDATE(), 1, 'TC', 20),
(N'Giỏ trái cây 02', 'products/giohoatraicay/giohoatraicay_02.jpg',  950000, GETDATE(), 1, 'TC', 20),
(N'Giỏ trái cây 03', 'products/giohoatraicay/giohoatraicay_03.jpg', 1000000, GETDATE(), 1, 'TC', 20),
(N'Giỏ trái cây 04', 'products/giohoatraicay/giohoatraicay_04.jpg', 1050000, GETDATE(), 1, 'TC', 20),
(N'Giỏ trái cây 05', 'products/giohoatraicay/giohoatraicay_05.jpg', 1100000, GETDATE(), 1, 'TC', 20),

-- Hoa để bàn (5)
(N'Hoa để bàn 01', 'products/hoadeban/hoadeban_01.jpg', 300000, GETDATE(), 1, 'DB', 20),
(N'Hoa để bàn 02', 'products/hoadeban/hoadeban_02.jpg', 320000, GETDATE(), 1, 'DB', 20),
(N'Hoa để bàn 03', 'products/hoadeban/hoadeban_03.jpg', 340000, GETDATE(), 1, 'DB', 20),
(N'Hoa để bàn 04', 'products/hoadeban/hoadeban_04.jpg', 360000, GETDATE(), 1, 'DB', 20),
(N'Hoa để bàn 05', 'products/hoadeban/hoadeban_05.jpg', 380000, GETDATE(), 1, 'DB', 20);
GO

-- Check số lượng sản phẩm (đúng phải = 100)
SELECT COUNT(*) AS TotalProducts FROM Products;
GO

-- ========== 4) ORDERS ==========
-- DB AsmJava5: cột Orders là (Id identity, Username, CreateDate default, Address)
-- Nên insert đúng thứ tự cột: (Username, Address) hoặc chỉ định rõ luôn
INSERT INTO Orders (Username, CreateDate, Address) VALUES
('user01', GETDATE(), N'Quận 1, TP.HCM'),
('user02', GETDATE(), N'Quận 3, TP.HCM'),
('user01', GETDATE(), N'Thủ Đức, TP.HCM');
GO

-- ========== 5) ORDER DETAILS ==========
INSERT INTO OrderDetails (OrderId, ProductId, Price, Quantity) VALUES
(1, 1, 350000, 2),
(1, 5, 390000, 1),
(2, 10, 440000, 1),
(2, 20, 540000, 2),
(3, 3, 370000, 1),
(3, 15, 490000, 1);
GO

-- ========== 6) QUICK SELECT ==========
SELECT * FROM Categories;
SELECT * FROM Products;
GO
