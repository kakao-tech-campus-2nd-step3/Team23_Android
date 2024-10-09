package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.repository.TransferRepository
import javax.inject.Inject

class TransferRepositoryImpl @Inject constructor() : TransferRepository {
    override suspend fun getTransferInfo(): List<TransferDetailItem> {
        // TODO: 일단 임시 데이터 반환
        return listOf(
            TransferDetailItem(
                "1",
                "라이언",
                5000,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcE9NGEQuAXw1Cg98jbhhhUfM2IpZcWilwHg&s"
            ),
            TransferDetailItem(
                "2",
                "춘식이",
                9000,
                "https://i.namu.wiki/i/GQMqb8jtiqpCo6_US7jmWDO30KfPB2MMvbdURVub61Rs6ALKqbG-nUATj-wNk7bXXWIDjiLHJxWYkTELUgybkA.webp"
            ),
            TransferDetailItem(
                "3",
                "네오",
                4000,
                "https://t4.daumcdn.net/thumb/R720x0.fjpg/?fname=http://t1.daumcdn.net/brunch/service/user/cnoC/image/r5RjC7tUqK6Mb4NVX0f5d-qrCSQ.jpg"
            )
        )
    }
}
