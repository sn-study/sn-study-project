# sn-study-project
| a | b | c |
|---|:---:|---:|
| 'a' |

## API 명세
1. 뿌리기 API
- URL : /ppurigi
- METHOD : POST
- HEADER : X-USER-ID, X-ROOM-ID
- 요청 파라미터

| 파라미터명 | 타입 | 설명 |
|---|---|---|
| reqCnt | Integer | 받아갈 대상의 숫자 |
| amount | Integer | 뿌린 금액 |
- 요청 예시
```
{
    "reqCnt" : 3,
    "amount" : 10000
}
```
- 응답 파라미터

| 파라미터명 | 타입 | 설명 |
|---|---|---|
| resultCode | String | 결과코드 |
| resultMessage | String | 결과메세지 |
| result | Object | 결과상 |
| result.token | String | 토큰 |

- 응답 예시
```
{
    "resultCode" : "SUCCESS",
    "resultMessage" :  "뿌리기 요청 성공",
    "result" : {
        token" : "ABC"
    }
}
```


2. 받기 API
3. 조회 API
