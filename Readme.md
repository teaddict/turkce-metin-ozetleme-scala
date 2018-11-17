Kelime Zinciri Algoritmasıyla Türkçe Metin Özetleme - SCALA
===================
---
### NOTLAR

Uzun bir aradan sonra nihayet fırsat bulup projeye geri dönüş yapabildim. Programın ilk sürümü bitirme projesi olduğu için biraz karışık ve aceleyle yazıldı. Büyüzden tüm projeyi scala dilinde yeniden yazdım. Hem daha anlaşılır hem daha kısa ve öz oldu. Spring MVC, Tomcat ve Postgresql üçlüsünü bırakarak , Finatra + Mongodb ikilisine geçtim. 
Şimdi ayrıca docker-compose ile özetleme servisini hızlıca kurabilirsiniz.

Algorıtma olarak çok bir değişiklik yapmadım, sadece daha önce farketmediğim hataları düzelttim, bu da performansı ve özet kalitesini artırdı. Metnin sınıfını bulma işlemini uygulamadan kaldırdım. İlk sürümde deneme amaçlı eklenmişti, suan kullanılmadığı için gerekli olduğunu düşünmüyorum.

### Algoritma
> **Kelime Zinciri Algoritması:**

Bu algoritma metnin ana konusunu belirmeye çalışmaktadır. Metindeki kelimeler arasında "anlamsal" bağ kurup, aynı anlama gelen kelimelerden bir zincir oluşturulmaktadır [9]. Daha sonra belirlenen puanlama ve sezgisel yöntemlere göre özet niteliği taşıyabilecek cümleleri seçerek belirlemektedir. Bu algoritma için en önemli şey güçlü bir "kelime ağının" var olmasıdır. Çünkü tüm zincirler kelimeler arasındaki ilişkilerden yola çıkarak oluşturulacağı için, güçlü bir kelime ağı belirsiz kalacak kelime sayısını azaltacak, güçlü zincirler kurulmasını sağlayacaktır. Türkçe için hazırlanmış tek kelime ağı Dr. Özlem Çetinoglu ve Dr. Kemal Oflazer tarafından 2004 yılında “BalkaNet” projesiyle oluşturulmuştur [10]. Tarafımızdan geliştirilecek bu proje kapsamında, wordnetin javaya aktarılması ve kullanılabilir hale getirilmesi gerekmekte idi. Öncelikle XML halinde bulunan bu listeyi çözümleyerek (parsing) yeniden oluşturulmuştur. Kullanılacak formata dönüşüm gerçekleştirilmiştir. Daha sonra Yıldız Teknik Üniversitesi Bilgisayar Mühendisliği bölümünden Emre Yıldız'ın oluşturduğu “Anlamsal İlişkiler Veri Kümesi” projesi dökumanları da kullanılacak formata dönüştürülmüştür [11]. Bu iki listeyi birleştirip kendi projemiz için ortak bir kelime ağı oluşturduk. Bu kelime ağı içindeki verileri ön aşamadan geçirdik, bu aşamalar;
>- Etkisiz kelimeler temizlendi.
>- Atasözler ve deyimler çıkartıldı.
>- Hyponymy ilişkiler, hypernymy ilişkilere dönüştürüldü. (Çünkü bu şekilde kelimelerin aranması kolaylaşmaktadır.)
>- Sıfatlar ve fiiller çıkartıldı.
>- Yer belirten adlar düzenlendi.
>- Terim listeleri eklendi.
>- Sayılar çıkartıldı.

Uygulamamızda tüm bu listeyi okuyup her kelime için bir ilişki listesi oluşturuyoruz, bir kelime girildiğinde bunun ilişkili olduğu kelimeler liste halinde kullanıcıya gönderilmektedir. Bu sistemin ileride ayrı bir servis olarak kullanıma sunulması planlanmaktadır. Böylece çevrimiçi Türkçe kelime ağı erişime açılmış olacaktır.

> **Zincirlerin puanlanması:**
> 
Bu aşamada zincirdeki kelimelerin aralarındaki ilişkiye göre puanlamasını gerçekleştirdik. Regina Barzilay tarafından hazırlanmış olan "Using Lexical Chains for Text Summarization [13]" doktora tezinden ve "Assessing the Impact of Lexical Chain Scoring Methods and Sentence Extraction Schemes on Summarization [14]" makalesinden faydalanılmıştır. Oluşturdukları puanlama sistemi kendi uygulamamıza göre değiştirilmiştir. Aşağıdaki şekilde bir puanlama sistemi oluşturulmuştur:

>- synonymy = 10
>- antonymy = 7
>- hypernymy = 4
>- hyponymy = 4
>- related_with =4
>- holo_part = 4
>- holo_member = 4
>- yan_kavram = 4

Ayrıca zincirlerin gücünü belirlemek için ayrı bir sistem daha kullanılmıştır. Bu sistem şu
aşamalardan oluşmaktadır:
>- Zincirdeki benzersiz kelimelerin sayısını bul
>- Homojenlik değerini bul = 1 - (benzersiz kelime sayısı / tüm kelimelerin sayısı)
>- Eşik değerini bul = ortalama puan + (2 ∗ puanların standart sapması)
>- Eşik değerinin üstündeki zincirleri güçlü zincirler olarak listeye al
###### 
> **Cümle Seçimi İşlemleri:**

>- Sezgisel Algoritma 1
Bu algoritma her zincirdeki ilk kelimenin yani zinciri başlatan kelimenin anahtar kelime olarak alınmasına dayanmaktadır. Bu anahtar kelimenin geçtiği ilk cümle tespit edilerek seçilmiştir.
>- Sezgisel Algoritma 2
Bu algoritma her zincirdeki kelimelerin frekansının hesaplanmasına dayanmaktadır. Öncelikle tüm kelimelerin frekansı hesaplanır ve zincirdeki kelimelerin frekans ortalaması bulunur. Ortalama frekansın üstündeki kelimeler işleme alınır. Bu kelimelerin ortak olarak geçtikleri bir cümle mevcut ise bu cümle seçilir, eğer hiçbir kelimenin kesiştiği bir cümle yoksa, en yüksek frekanslı cümlenin geçtiği cümle alınır.
>- Sezgisel Algoritma 3
Bu algoritma her zincirin yoğunlaştığı paragrafı bulmaya ve bu paragrafta zincirdeki
kelimeleri içeren cümle frekansına dayanmaktadır. Eğer tüm kelimeler aynı paragraftaysa doğrudan bu paragraftaki cümlelerin analizi yapılır. Eğer zincirdeki kelimeler farklı paragraflardaysa öncelikle paragrafların frekansları alınır. En yüksek frekanslı paragraf seçilir ve bu paragraftaki cümlelerin analizi yapılır. Cümle analizi, öncelikle zincirdeki kelimelerin hangi cümlelerde geçtiği bilindiği için bu kelimeler paragraflara göre ayıklanır ve her paragraf içinde bu kelimelerin ait oldukları cümlelerin frekansı alınır. En yüksek frekanslı cümleyi o zincir için seçilmektedir.

------

>- [10] Stamou, S., Oflazer, K., Pala, K., Christodoulakis, D., Cristea, D., Tufis, D., Koeva, S.,Totkov,G., Dutoit, D., Grigoriadou, M.: Balkanet: A multilingual semantic network for Balkan languages.Proceedings of the 1st Global Wordnet Conference. Mysore, Hindistan, (2002).
>- [11] Emre Yıldız, “Anlamsal İlişkiler Veri Kümesi”, Yıldız Teknik Üniversitesi, Bilgisayar Müh.
Bölümü,(2010).
>- [12] Oğuz Yıldırım, Fatih Atık, M. Fatih AMASYALI, "42 Bin Haber Veri Kümesi”, Yıldız Teknik
Üniversitesi, Bilgisayar Müh. Bölümü,(2003).
>- [13] Regina Barzilay and Michael Elhadad, “Using Lexical Chains for Text Summarization”, In
Proceedings of the ACL Workshop on Intelligent Scalable Text Summarization,(1997), 10-17.
>- [14] William Doran, Nicola Stokes, Joe Carthy, John Dunnion. "Assessing the Impact of Lexical Chain Scoring Methods and Sentence Extraction Schemes on Summarization", Computational Linguistics and Intelligent Text Processing Volume 2945 of the series Lecture Notes in Computer Science , (2004), 627-635.

---
##### 
> **Gereksinimler:** 
>- JavaSE 1.8
>- MongoDb
>- Docker & Docker Compose

> **Sunucu kurulumu :** 
>- LINUX
>- Terminal ile öncelikle uygulama dizinine geciyoruz
>- eger armv7 kullaniyorsak:  sbt 'set dockerBaseImage := "armv7/armhf-java8"' docker:stage
>- eger kullanmiyorsak oracle veya openjdk secebilirsiniz: sbt 'set dockerBaseImage := "nimmis/java:oracle-8-jdk"' docker:stage
>- sudo docker-compose -f docker-compose.yml up -d --build
>- localhost:9999 adresinden sunucuya ulasabiliriz
>- sunucuyu durdurmak icinse
>- sudo docker-compose down

----------

###Örnek

> **Haber metni:**

Yıldız Kızlarımız Dünya Şampiyonu

Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu. Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti.

Yıldız Milli Takım, TVF Başkent Salonu'nda yapılan final maçında baştan sona üstün bir performans sergileyerek, Dünyanın en iyi takımları arasında yer alan Çin'e adeta göz açtırmadı. Tüm oyuncuların iyi oynadığı Türk Milli Takımı'nda Kübra Akman performansıyla göz doldururken, Çin Milli Takımı'nın solak smaçörü Peiyi Liu, Yıldız kızları zorlayan en önemli oyuncu oldu. Türkiye, 2007 yılında Meksika'da yapılan Dünya Yıldız Kızlar Şampiyonası finalinde Çin'e karşı 3-1 kaybederek Dünya ikincisi olduğu maçın rövanşını set kayıpsız aldı.

Bu arada karşılaşmayı Gençlik ve Spor Bakanı Suat Kılıç, Türkiye Voleybol Federasyonu Başkanı Erol Ünal Karabıyık ile birlikte protokol tribününden takip etti. TVF Başkent Salonu'nun tamamını dolduran seyirciler, ellerindeki Türk bayraklarıyla maç boyunca Türk Milli Takımı'nı coşkulu bir şekilde desteklediler.Voleybolseverler, TVF Bandosunun çaldığı hareketli parçalara eşlik ederek, takımlarını bir an bile yalnız bırakmadılar.

Yıldız Kızlar Dünya Şampiyonası FIVB'nin düzenlediği ve 18 yaşının altındaki voleybolcuların katılabildiği bir şampiyonadır.  İlk şampiyona 1989 yılında Brezilya'nın Curitiba kentinde yapılmıştır. Her iki yılda bir düzenlenen şampiyonaya kıta elemelerini geçen ülke takımları katılabilmektedir.

> **Özet:**
[Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu., Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti., Yıldız Milli Takım, TVF Başkent Salonu'nda yapılan final maçında baştan sona üstün bir performans sergileyerek, Dünyanın en iyi takımları arasında yer alan Çin'e adeta göz açtırmadı.] 

> **Kelime Zincirleri**
>- "P1 S2" kelimenin 1. paragrafdaki 2. cümlede  geçtiğini belirtir
>
>- (şampiyon synonymy kişi) P0-S0,(yıldız hypernymy kişi) P1-S0,(şampiyon synonymy kişi) P1-S0,(şampiyon synonymy kişi) P1-S1,(el hypernymy kişi) P1-S1,(baş hypernymy kişi) P2-S0,(üst hypernymy kişi) P2-S0,(türk hypernymy kişi) P2-S1,(başkan hypernymy kişi) P3-S0,(el hypernymy kişi) P3-S1,(türk hypernymy kişi) P3-S1,(türk hypernymy kişi) P3-S1,
>- Zincirdeki kelime sayisi: 12
>- Zincirin iliskisel puan degeri: 66
>- Zincirin guc degeri: 5

>- (mil related_with matematik) P1-S0,(mil related_with matematik) P2-S0,(mil related_with matematik) P2-S1,(mil related_with matematik) P2-S1,(mil related_with matematik) P3-S1,(şekil related_with matematik) P3-S1,:6:24:4.0
>- Zincirdeki kelime sayisi: 6
>- Zincirin iliskisel puan degeri: 24
>- Zincirin guc degeri: 4.0

>- (dünya holo_member güneş sistemi) P0-S0,(dünya holo_member güneş sistemi) P1-S0,(dünya holo_member güneş sistemi) P1-S1,(dünya holo_member güneş sistemi) P2-S0,(dünya holo_member güneş sistemi) P2-S2,(dünya holo_member güneş sistemi) P2-S2,(dünya holo_member güneş sistemi) P4-S0,:7:28:6.0
>- Zincirdeki kelime sayisi: 7
>- Zincirin iliskisel puan degeri: 28
>- Zincirin guc degeri: 6.0

>- (dünya related_with astronomi) P0-S0,(yıldız holo_member astronomi) P1-S0,(yıldız hypernymy astronomi) P1-S0,(yıldız holo_member astronomi) P1-S0,(yıldız holo_member astronomi) P2-S0,(yıldız holo_member astronomi) P2-S1,(yıl related_with astronomi) P2-S2,(yıldız holo_member astronomi) P2-S2,(yıldız holo_member astronomi) P4-S0,(yıl related_with astronomi) P4-S1,(yıl related_with astronomi) P4-S2,:11:44:8.0
>- Zincirdeki kelime sayisi: 11
>- Zincirin iliskisel puan degeri: 44
>- Zincirin guc degeri: 8.0

>- (çin hypernymy ülke) P1-S0,(türkiye holo_member ülke) P1-S1,(türkiye holo_part ülke) P1-S1,(türkiye hypernymy ülke) P1-S1,(el synonymy ülke) P1-S1,(türkiye holo_member ülke) P2-S2,(meksika hypernymy ülke) P2-S2,(türkiye holo_member ülke) P3-S0,(brezilya hypernymy ülke) P4-S1,(kıta synonymy ülke) P4-S2,:10:52:4.0
>- Zincirdeki kelime sayisi: 10
>- Zincirin iliskisel puan degeri: 52
>- Zincirin guc degeri: 4.0

>- (voleybol related_with spor) P1-S0,(takım related_with spor) P1-S0,(final related_with spor) P1-S0,(voleybol related_with spor) P1-S1,(final related_with spor) P2-S0,(oyun related_with spor) P2-S1,(smaçör related_with spor) P2-S1,(oyun related_with spor) P2-S1,(final related_with spor) P2-S2,(set related_with spor) P2-S2,(voleybol related_with spor) P3-S0,(voleybol related_with spor) P4-S0,:12:48:6.0
>- Zincirdeki kelime sayisi: 12
>- Zincirin iliskisel puan degeri: 48
>- Zincirin guc degeri: 6.0

>- (dünya synonymy grup) P0-S0,(takım synonymy grup) P1-S0,(takım synonymy grup) P2-S0,(takım synonymy grup) P2-S0,(takım synonymy grup) P2-S1,(takım synonymy grup) P2-S1,(takım synonymy grup) P3-S1,(takım synonymy grup) P3-S2,(takım synonymy grup) P4-S2,:9:90:7.0
>- Zincirdeki kelime sayisi: 9
>- Zincirin iliskisel puan degeri: 90
>- Zincirin guc degeri: 7.0

>- (şampiyona synonymy bökelik) P1-S0,(şampiyona synonymy bökelik) P2-S2,(şampiyona synonymy bökelik) P4-S0,(şampiyona synonymy bökelik) P4-S0,(şampiyona synonymy bökelik) P4-S1,(şampiyona synonymy bökelik) P4-S2,:6:60:5.0
>- Zincirdeki kelime sayisi: 6
>- Zincirin iliskisel puan degeri: 60
>- Zincirin guc degeri: 5.0

>
>- <b> TOPLAM SONUCLAR</b>
>- Tüm zincirler: 429
>- Benzersiz zincirler: 98
>- Güçlü zincirler: 8
>- Kelime zinciri ortalama puan değeri: 15.33673469387755
>- Kelime zinciri ortalama güç değeri: 0.5918367346938775
>- Kelime zinciri kriter değeri: 3.8724132730465493

> **JSON API RESPOND:**
>- {<b>"result"</b>:"[Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu., Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti., Yıldız Milli Takım, TVF Başkent Salonu'nda yapılan final maçında baştan sona üstün bir performans sergileyerek, Dünyanın en iyi takımları arasında yer alan Çin'e adeta göz açtırmadı.] "
}

----------
### API Kullanım

>- [https://teaddict.net/ozetle](https://teaddict.net/ozetle) adresinden özetleme işlemini browser üzerinden yapabilirsiniz.
>- [https://turkcemetinozetleme.teaddict.net](https://turkcemetinozetleme.teaddict.net/) adresinde swagger mevcut
>- Örnek API kullanım için [bu dokümanları](https://github.com/teaddict/turkce-metin-ozetleme-scala/tree/master/ornek-api-kullanim) inceleyebilirsiniz.
>- [Örnek metin dosyaları](https://github.com/teaddict/turkce-metin-ozetleme/tree/master/ornek-metinler)
