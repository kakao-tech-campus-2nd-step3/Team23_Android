# KAPP짱 - 正산
경북대 23조 안드로이드

# 💬 9주차 코드리뷰 받고 싶은 부분

## 토큰 관리 관련

Retrofit을 통해 백엔드에 Request를 보낼 때마다 JWT를 헤더에 담아 보내야 합니다.
   
JWT는 Datastore 내지는 EncryptedSharedPreferences에 저장하고, `:common-domain` 모듈 내 ServerAuthenticationRepository에 관련 인터페이스를 담고 있습니다.

백엔드 API를 호출하는 과정에서 JWT를 저장, 참조, 관리하는 로직은 어떤 방식으로 구현하면 좋을 지 궁금합니다.

일단은 UseCase에서 서버와 통신하는 리포지토리의 메소드를 호출하기 전 ServerAuthenticationRepository에서 JWT를 불러와 메소드 파라미터로 넘기는 방식을 생각하고 있습니다.
