package com.exparo.data.repository.fake

import com.exparo.base.TestDispatchersProvider
import com.exparo.data.DataObserver
import com.exparo.data.repository.RepositoryMemoFactory
import org.jetbrains.annotations.VisibleForTesting

@VisibleForTesting
fun fakeRepositoryMemoFactory(): RepositoryMemoFactory = RepositoryMemoFactory(
    dataObserver = DataObserver(),
    dispatchers = TestDispatchersProvider
)