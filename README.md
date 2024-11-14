# KAPP짱 - 正산
우리 모임의 지출 관리를 쉽고 편하게!

![正산 (1)](https://github.com/user-attachments/assets/caa81f9b-b6d4-439a-8623-adab9cbfcb20)

---

## 구현 기능
|팀원|담당 파트|
|---|---|
|권성찬|지출 목록 조회, 지출 상세 내역 조회, 지출 추가, 카메라 촬영 및 OCR 전송 UI 구현   <br> 지출 목록 조회, 지출 상세 내역 조회, 지출 정보 추가 및 수정 관련 도메인, 데이터 로직 구현<br>CI 스크립트 구현|
|주수민|*|
|정수현|*|

### [User 관련]

- **로그인 & 회원가입**
    - 카카오 API를 통한 카카오 로그인 기능
    - 서비스 서버에서 로그인 및 회원가입 후 토큰 발급 및 저장
    - 저장된 토큰으로 자동 로그인 기능 지원
        ![로그인 흐름 (4)](https://github.com/user-attachments/assets/438943f5-f9a7-4962-a337-c3bef3fb0a52)

### [Expense 관련]

- **지출 내역 등록**
    - 영수증 촬영 또는 수기 입력을 통한 지출 상세 내역 등록
    - 카테고리 선택(Dialog) 기능 제공
    - 영수증 인식 시 잘못 인식된 정보 수정 가능
- **지출 목록 조회**
    - 진행 상태별(정산 중, 송금 대기, 송금 완료)로 지출 목록 분류
    - 본인이 등록한 지출 여부 확인 기능
    - 정산 중인 지출의 경우, 해당 지출의 확인 여부 표시 기능
- **개인 소비 내역 저장 및 수정**
    - 등록된 지출 내역 중 개인 소비 항목 선택 및 저장
    - 기존 소비 내역 수정 기능 제공
- **지출 선택 현황 확인**
    - 내가 등록한 지출에 대해 다른 모임원의 소비 내역 확인 가능
    - 모임원의 소비 내역을 검토 후, 상태를 `정산 중` ↔ `송금 대기`로 전환 가능
- **송금 요청**
    - 내가 등록한 지출에 대한 다른 모임원들의 송금할 금액 조회 기능
    - 카카오톡 메시지를 통한 송금 요청 메시지 전송 기능
    - *(요청 메시지의 링크를 통해 카카오페이 송금으로 연결되는 기능)*
        - 송금 링크 생성을 위해서는 카카오페이 디벨로퍼 사업자 등록 필요로 인해 구현하지 못함

### [Group 관련]

- **모임 생성**
    - 카카오 친구 선택을 통한 모임 생성
    - 모임원에게 초대 메시지 전송 기능 포함
- **모임 조회**
    - 진행 중인 모임과 완료된 모임 목록 조회 기능
- **모임 초대 메시지 전송**
    - 모임 생성 시 카카오톡 초대 메시지 전송
    - 초대 링크를 통해 앱 내 모임 참여 가능
    - `모임 > 초대 현황`에서 가입하지 않은 모임원에게 초대 메시지 재전송
- **모임 관리**
    - `모임 > 모임 종료` 클릭 시 모임을 완료 상태로 변경
- **모임원 카카오 ID 조회**
    - 모임원의 카카오 ID를 조회하여 친구 목록과 대조 후 UUID 획득
    - 획득한 UUID로 카카오톡 메시지 전송 가능

### [Ocr 관련]

- **영수증 내용 분석**
    - 촬영한 영수증을 서버로 전송하여 지출 상세 정보를 자동 추출 및 표시

---

## 🎞️ 데모 영상

https://github.com/user-attachments/assets/1928d49c-d904-43af-b25c-588098ecb733

https://github.com/user-attachments/assets/b2cce39f-9364-4419-82ca-a70634b5c0f0

https://github.com/user-attachments/assets/0994d120-5f47-44e0-a815-d7da47d94c67

https://github.com/user-attachments/assets/46761e9f-efea-4650-8bb6-0bff96314cfd

https://github.com/user-attachments/assets/3b7e0ff0-19f7-4531-bd6d-0c217ed4c3a3

https://github.com/user-attachments/assets/f721366b-b973-408e-a332-284d17257f97

https://github.com/user-attachments/assets/e5da715b-1a3b-4594-9c85-5885f7fc2d19

https://github.com/user-attachments/assets/0405758d-b54f-4711-a0d6-baa58cfaf977

https://github.com/user-attachments/assets/1783cb53-93b1-4d24-80c6-052b091339b1

---

## 기술 스택
![Android Tech Stack](https://github.com/user-attachments/assets/5280814b-dd68-48fa-9298-ea4aed326f6e)

---

## 프로젝트 모듈 구조
```
📦23조 正산 - Android Module Structure
├─🟢app				
├─🔵common
│  ├─🔵androidutil		
│  ├─🔵datastore			
│  ├─🔵dispatcher			
│  ├─🔵kakaoclient		
│  ├─🔵navigation			
│  ├─🔵resource				
│  ├─🔵retrofit				
│  └─🔵util
├─🔵data
│  ├─🔵expense		
│  ├─🔵group			
│  ├─🔵ocr				
│  └─🔵user				
├─🔵domain
│  ├─🔵common-user		
│  ├─🔵expense				
│  ├─🔵group					
│  └─🔵ocr						
└─🟢ui
    ├─🟢addexpense				
    ├─🟢camera						
    ├─🟢creategroup				
    ├─🟢data							
    ├─🟢expensedatail			
    ├─🟢expenselist				
    ├─🟢login							
    ├─🟢main							
    └─🟢sendmessage
```
