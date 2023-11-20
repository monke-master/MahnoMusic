package com.monke.machnomusic3.domain.model

import com.monke.machnomusic3.data.extensions.milliseconds
import java.util.UUID


val mockedUser1 = User(
    id = UUID.randomUUID().toString(),
    username = "Русич",
    login = "Russich",
    password = "",
    email = "Elizarov@staling.org",
    bio = "lslslls",
    profilePicId = "1234"
)
val mockedTracks = arrayOf(
    Track(
        id = "07b16a5a-86dc-4ee2-b73e-ea3e48412bd6",
        title = "Встает Ярило",
        author = mockedUser1,
        coverId = "",
        duration = 163.milliseconds(),
        releaseDate = 0
    ),
    Track(
        id = "1ef0d08e-e600-4582-8869-fbd8bbb97323",
        title = "Трек 2",
        author = mockedUser1,
        coverId = "",
        duration = 163.milliseconds(),
        releaseDate = 0
    ),
    Track(
        id = "4bcda889-e3b7-47f5-b09c-2a023e62f759",
        title = "Трек 3",
        author = mockedUser1,
        coverId = "",
        duration = 163.milliseconds(),
        releaseDate = 0
    ),
    Track(
        id = "397b6c2b-7cfb-4a4b-85aa-7fcfb31eebde",
        title = "Трек 4",
        author = mockedUser1,
        coverId = "",
        duration = 163.milliseconds(),
        releaseDate = 0
    ),
    Track(
        id = "07b16a5a-86dc-4ee2-b73e-ea3e48412bd6",
        title = "Встает Ярило",
        author = mockedUser1,
        coverId = "",
        duration = 163.milliseconds(),
        releaseDate = 0
    ),
)
