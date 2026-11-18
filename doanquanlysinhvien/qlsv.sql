CREATE LOGIN quanlysv_user WITH PASSWORD = '123456';
GO

USE doanqlsinhvien;
GO
CREATE USER quanlysv_user FOR LOGIN quanlysv_user;
GO
ALTER ROLE db_owner ADD MEMBER quanlysv_user;
GO


CREATE TABLE khoa (
    ma_khoa VARCHAR(20) PRIMARY KEY,
    ten_khoa NVARCHAR(100) NOT NULL
);


GO

CREATE TABLE lop (
    ma_lop VARCHAR(20) PRIMARY KEY,
    ten_lop NVARCHAR(100) NOT NULL,
    ma_khoa VARCHAR(20),
    CONSTRAINT FK_lop_khoa FOREIGN KEY (ma_khoa) 
        REFERENCES khoa(ma_khoa) ON DELETE CASCADE
);
GO

CREATE TABLE giang_vien (
    ma_gv VARCHAR(20) PRIMARY KEY,
    ho_ten NVARCHAR(100) NOT NULL,
    gioi_tinh NVARCHAR(10),
    ngay_sinh DATE,
    email VARCHAR(100) UNIQUE,
    so_dien_thoai VARCHAR(15),
    ma_khoa VARCHAR(20),
    CONSTRAINT FK_giang_vien_khoa FOREIGN KEY (ma_khoa) 
        REFERENCES khoa(ma_khoa)
);
GO

CREATE TABLE sinh_vien (
    ma_sv VARCHAR(20) PRIMARY KEY,
    ho_ten NVARCHAR(100) NOT NULL,
    gioi_tinh NVARCHAR(10),
    ngay_sinh DATE,
    dia_chi NVARCHAR(255),
    email VARCHAR(100) UNIQUE,
    so_dien_thoai VARCHAR(15),
    ma_lop VARCHAR(20),
    CONSTRAINT FK_sinh_vien_lop FOREIGN KEY (ma_lop) 
    REFERENCES lop(ma_lop) ON DELETE CASCADE
);

GO

CREATE TABLE mon_hoc (
    ma_mh VARCHAR(20) PRIMARY KEY,
    ten_mh NVARCHAR(100) NOT NULL,
    so_tin_chi INT NOT NULL,
    ma_khoa VARCHAR(20),
    CONSTRAINT FK_mon_hoc_khoa FOREIGN KEY (ma_khoa) 
        REFERENCES khoa(ma_khoa)
);
GO

CREATE TABLE phan_cong (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_gv VARCHAR(20),
    ma_mh VARCHAR(20),
    hoc_ky VARCHAR(20) NOT NULL,
    CONSTRAINT FK_phan_cong_gv FOREIGN KEY (ma_gv) 
        REFERENCES giang_vien(ma_gv) ON DELETE CASCADE,
    CONSTRAINT FK_phan_cong_mh FOREIGN KEY (ma_mh) 
        REFERENCES mon_hoc(ma_mh) ON DELETE CASCADE
);
GO

CREATE TABLE ket_qua (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_sv VARCHAR(20),
    ma_mh VARCHAR(20),
    hoc_ky VARCHAR(20) NOT NULL,
    diem_qua_trinh FLOAT,
    diem_thi FLOAT,
    diem_tong_ket FLOAT,
    diem_chu VARCHAR(5),       
    ket_qua NVARCHAR(20),      
    
    CONSTRAINT FK_ket_qua_sv FOREIGN KEY (ma_sv) 
    REFERENCES sinh_vien(ma_sv) ON DELETE CASCADE,
    CONSTRAINT FK_ket_qua_mh FOREIGN KEY (ma_mh) 
    REFERENCES mon_hoc(ma_mh) ON DELETE CASCADE
);
GO

CREATE TABLE tai_khoan (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL 
);
GO
INSERT INTO tai_khoan 
VALUES ('admin', '123456', 'ADMIN');
INSERT INTO khoa
VALUES ('CNTT', N'Công nghệ thông tin')
INSERT INTO khoa
VALUES ('Yk', N'Y Khoa')
SET DATEFORMAT dmy;
delete from sinh_vien
SELECT * FROM ket_qua;
SELECT * FROM mon_hoc;
SELECT * FROM sinh_vien;
