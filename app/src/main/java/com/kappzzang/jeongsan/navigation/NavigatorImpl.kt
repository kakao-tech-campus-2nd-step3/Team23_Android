package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent
import javax.inject.Inject

class NavigatorImpl @Inject constructor(private val context: Context) : AppNavigator {
    override fun navigateToMainPage(): Intent {
        TODO("Not yet implemented")
    }

    override fun navigateToCreateGroup(): Intent {
        TODO("Not yet implemented")
    }

    override fun navigateToExpenseDetail(): Intent {
        TODO("Not yet implemented")
    }

    override fun navigateToExpenseList(): Intent {
        TODO("Not yet implemented")
    }

    override fun navigateToCamera(): Intent {
        TODO("Not yet implemented")
    }

    override fun navigateToLogin(): Intent {
        TODO("Not yet implemented")
    }
}
