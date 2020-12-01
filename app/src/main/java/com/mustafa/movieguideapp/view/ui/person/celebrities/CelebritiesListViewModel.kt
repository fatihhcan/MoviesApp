package com.mustafa.movieguideapp.view.ui.person.celebrities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.filter
import androidx.paging.rxjava2.cachedIn
import com.mustafa.movieguideapp.repository.people.PeopleRepository
import com.mustafa.movieguideapp.testing.OpenForTesting
import javax.inject.Inject


@OpenForTesting
class CelebritiesListViewModel @Inject constructor(
    repository: PeopleRepository,
) : ViewModel() {

    val peopleStream = repository.loadPopularPeople()
        .map { pagingData -> pagingData.filter { it.profile_path != null } }
        .cachedIn(viewModelScope)
}