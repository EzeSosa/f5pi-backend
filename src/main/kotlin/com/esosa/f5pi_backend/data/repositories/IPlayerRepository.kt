package com.esosa.f5pi_backend.data.repositories

import com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse
import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.Player
import com.esosa.f5pi_backend.data.models.Season
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID


interface IPlayerRepository: JpaRepository<Player, UUID> {
    @Query("SELECT new com.esosa.f5pi_backend.controllers.responses.PlayerStatisticsResponse("
            + "CAST(COUNT(g) AS long), "
            + "CAST(SUM(m.goalsScored) AS long), "
            + "CAST(SUM(CASE WHEN t.winner = true THEN 1 ELSE 0 END) AS long), "
            + "CAST(SUM(CASE WHEN g.official = true THEN 1 ELSE 0 END) AS long), "
            + "CAST(SUM(CASE WHEN g.official = true THEN m.goalsScored ELSE 0 END) AS long), "
            + "CAST(SUM(CASE WHEN g.official = true AND t.winner = true THEN 1 ELSE 0 END) AS long), "
            + "CAST(SUM(g.individualPrice) AS long)) "
            + "FROM Player p "
            + "INNER JOIN Member m ON m.player = p "
            + "INNER JOIN Team t ON m.team = t "
            + "INNER JOIN GameDetails gd ON t.gameDetails = gd "
            + "INNER JOIN Game g ON g.details = gd "
            + "WHERE (p = ?1) "
            + "AND (?2 is null OR g.field = ?2) "
            + "AND (?3 is null OR g.season = ?3)")
    fun getPlayerStatistics(player: Player, field: Field?, season: Season?): PlayerStatisticsResponse
}