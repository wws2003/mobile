package hpg.org.samplegithubrepobrowser.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import hpg.org.samplegithubrepobrowser.model.dto.Project
import hpg.org.samplegithubrepobrowser.model.entity.InterestedProjectEntity
import hpg.org.samplegithubrepobrowser.model.repository.db.InterestedProjectDao
import hpg.org.samplegithubrepobrowser.util.TaskControl
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import java.util.concurrent.Callable

class InterestedProjectRepositoryImpl(private val dao: InterestedProjectDao) : InterestedProjectRepository {

    override fun saveInterestedProjects(projects: List<Project>): LiveData<List<Long>> {
        // TODO Implement properly
        return getLiveDataFromDao(Function { daoInstance ->
            val savedIds = ArrayList<Long>()
            for (project in projects) {
                val entity = InterestedProjectEntity(project.owner!!.id, project.id, project.name)
                // TODO Handle the case insert failed by throwing exception
                if (daoInstance.insert(entity) > 0) {
                    savedIds.add(project.id)
                }
            }

            savedIds
        })
    }

    override fun loadInterestedProjects(): LiveData<List<Project>> {
        return getLiveDataFromDao(Function { daoInstance ->
            // Select by userName
            val interestedProjectEntities = daoInstance.selectAll()
            val interestedProjects = ArrayList<Project>()
            for (projectEntity in interestedProjectEntities) {
                val project = Project()
                project.id = projectEntity.projectId
                interestedProjects.add(project)
            }
            interestedProjects
        })
    }

    private fun <T> getLiveDataFromDao(dataFunc: Function<InterestedProjectDao, T>): LiveData<T> {
        val ret = MutableLiveData<T>()

        TaskControl.executeInBackgroundThenProcessResult(
            Callable<T> {
                dataFunc.apply(dao)
            },
            Consumer { results ->
                run {
                    ret.postValue(results)
                }
            }
        )

        return ret
    }
}