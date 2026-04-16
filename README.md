# Harcama Takip Uygulaması

Kişisel harcamaları kolayca takip edebilmek için geliştirilmiş bir web uygulamasıdır.  
Toplam harcamaları, kategori bazlı dağılımları ve tarih filtreleme özelliklerini içerir.  
Frontend kısmı Thymeleaf ile hazırlanmış, backend kısmı Spring Boot kullanılarak geliştirilmiştir.  
Veriler MySQL veritabanında saklanmaktadır.

---

## Özellikler
- 💰 Toplam harcamaları görüntüleme
- 📊 Kategori bazlı harcama grafikleri (Pie ve Donut chart)
- 📅 Harcamaları tarihe göre filtreleme
- 🗄️ MySQL veritabanı entegrasyonu
- 🐳 Docker ve Jenkins ile CI/CD entegrasyonu

---

## Kurulum

### Gereksinimler
- Java 17+
- Maven
- Docker & Docker Compose
- MySQL

### Çalıştırma
1. Projeyi klonla:
   ```bash
   git clone https://github.com/<kullanıcı-adı>/harcama-takip.git
   cd harcama-takip
