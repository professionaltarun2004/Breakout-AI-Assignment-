package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LeadDao {
    @Query("SELECT * FROM leads ORDER BY receivedAt DESC")
    fun getAllLeads(): Flow<List<LeadEntity>>

    @Query("SELECT * FROM leads WHERE id = :id LIMIT 1")
    fun getLeadById(id: String): Flow<LeadEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: LeadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeads(leads: List<LeadEntity>)

    @Update
    suspend fun updateLead(lead: LeadEntity)
}

@Dao
interface EscalationDao {
    @Query("SELECT * FROM escalations")
    fun getAllEscalations(): Flow<List<EscalationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEscalations(escalations: List<EscalationEntity>)

    @Query("DELETE FROM escalations WHERE id = :id")
    suspend fun deleteEscalationById(id: String)
}

@Dao
interface FollowUpDao {
    @Query("SELECT * FROM followups ORDER BY dueAt ASC")
    fun getAllFollowUps(): Flow<List<FollowUpEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowUps(followUps: List<FollowUpEntity>)

    @Update
    suspend fun updateFollowUp(followUp: FollowUpEntity)
}
