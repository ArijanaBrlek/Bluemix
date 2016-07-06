# Prepoznavanje artefakta na slici te izgovaranje artefakta na više jezika

Ideja aplikacije je da se fotografira određeni predmet i da aplikacija prepozna o kojem
predmetu na slici je riječ te da naziv predmeta / pojma potom ispiše na engleskom te
ta da se pusti izgovor teksta za nazivom predmeta/pojma. Koristi se IBM Bluemix
Watson cloud rješenje za prepoznavanje uz osnovne funkcionalnosti:
- fotografiranje predmeta (upaliti kameru te napraviti fotografiranje te pohranu slike u
JPEG format)
- slanje slike predmeta na Bluemix server (detalje API poziva slijede u nastavku) uz
korištenje Basic autentikacije te parsiranje odgovora
- ispis navjerojatnijeg   naziva   predmeta/pojma   na   engleskom   na   telefonu   te
pokretanje opcije puštanja zvučnog zapisa sa izgovorom predmeta na engleskom
(zvučni zapis dobaviti putem poziva prema Bluemix serveru)
- dodatna tema bi bila da se napravi aplikacija koja samo izgovara unešeni
tekst no prije toga omogućava korisniku odabir jezika (Bluemix podržava više jezika) te
odabir glasa koji izgovara tekst (muški ženski)

Visual recognition service:  
url do servisa:
https://gateway-a.watsonplatform.net/visual-recognition/api  
api dokumentacija: https://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/visual-recognition/api/v3/#introduction

Text to speech service:  
url:
https://stream.watsonplatform.net/text-to-speech/api  
api dokumentacija: https://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/text-to-speech/api/v1/#introduction
