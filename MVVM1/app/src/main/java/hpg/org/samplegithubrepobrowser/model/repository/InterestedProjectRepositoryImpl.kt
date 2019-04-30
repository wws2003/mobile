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

    override fun saveInterestedProject(project: Project): LiveData<Long> {
        // TODO Implement properly
        return getLiveDataFromDao(Function { daoInstance ->
            val entity = InterestedProjectEntity(project.userName, project.id, project.name)
            daoInstance.insert(entity)
        })
    }

    override fun loadInterestedProjects(userName: String): LiveData<List<Project>> {
        return getLiveDataFromDao(Function { daoInstance ->
            // Select by userName
            val interestedProjectEntities = daoInstance.selectByUser(userName)
            val interestedProjects = ArrayList<Project>()
            for (projectEntity in interestedProjectEntities) {
                val project = Project(userName)
                project.id = projectEntity.projectID
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