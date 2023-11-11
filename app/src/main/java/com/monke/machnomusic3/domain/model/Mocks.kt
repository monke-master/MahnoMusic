package com.monke.machnomusic3.domain.model

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
        coverId = ""
    ),
    Track(
        id = "07b16a5a-86dc-4ee2-b73e-ea3e48412bd6",
        title = "Встает Ярило",
        author = mockedUser1,
        coverId = ""
    ),
    Track(
        id = "07b16a5a-86dc-4ee2-b73e-ea3e48412bd6",
        title = "Встает Ярило",
        author = mockedUser1,
        coverId = ""
    ),
    Track(
        id = "07b16a5a-86dc-4ee2-b73e-ea3e48412bd6",
        title = "Встает Ярило",
        author = mockedUser1,
        coverId = ""
    ),
    Track(
        id = "07b16a5a-86dc-4ee2-b73e-ea3e48412bd6",
        title = "Встает Ярило",
        author = mockedUser1,
        coverId = ""
    ),
)
