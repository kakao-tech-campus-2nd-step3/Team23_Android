package com.kappzzang.jeongsan.creategroup.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.creategroup.CreateGroupActivity
import com.kappzzang.jeongsan.navigation.CreateGroupNavigator

class CreateGroupNavigatorImpl : CreateGroupNavigator {
    override fun navigateToCreateGroup(packageContext: Context): Intent =
        Intent(packageContext, CreateGroupActivity::class.java)
}
