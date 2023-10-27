import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

//***************************   Вопросы: Cancellation **********************************************

//Вопрос 1
//Ответ на вопрос 1: не отработает, т.к. отменяется родительская корутина job до завершения работы дочерних,
// следовательно дочерние не закончат свою работу

//fun main() {
//    runBlocking {
//        val job = CoroutineScope(EmptyCoroutineContext).launch {
//            launch {
//                //println("start1")
//                delay(500)
//                println("ok1") // <--
//            }
//            launch {
//                //println("start2")
//                delay(500)
//                println("ok2")
//            }
//        }
//        delay(100)
//        job.cancelAndJoin()
//    }
//}

//Вопрос 2
//Ответ на вопрос 2: не отработает, т.к. отмена этой корутины child.cancel() произойдет быстрее (корутина запустится, но не завершится)

//fun main() = runBlocking {
//    val job = CoroutineScope(EmptyCoroutineContext).launch {
//        val child = launch {
//            //println("start")
//            delay(500)
//            println("ok1") // <--
//        }
//        launch {
//            delay(500)
//            println("ok2")
//        }
//        delay(100)
//        child.cancel()
//    }
//    delay(100)
//    job.join()
//}


// *******************************  Вопросы: Exception Handling ************************************

//Вопрос 1
//Ответ на вопрос 1: нет, такой код не перехватит никаких исключений
//корутина запускается внутри try/catch, в таком сл., исключение, выброшенное в дочерней корутине, в родительской перехвачено не будет

//fun main() {
//    with(CoroutineScope(EmptyCoroutineContext)) {
//        try {
//            launch {
//                throw Exception("something bad happened")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace() // <--
//        }
//    }
//    Thread.sleep(1000)
//}

//Вопрос 2
//Ответ на вопрос 2: да, сработает. Исключение перехватывается в блоке catch.

//fun main() {
//    CoroutineScope(EmptyCoroutineContext).launch {
//        try {
//            coroutineScope {
//                throw Exception("something bad happened")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace() // <--
//        }
//    }
//    Thread.sleep(1000)
//}

//Вопрос 3
//Ответ на вопрос 3: да, т.к. при supervisorScope исключения в дочерних корутинах не влияют на соседние дочерние и родительскую

//fun main() {
//    CoroutineScope(EmptyCoroutineContext).launch {
//        try {
//            supervisorScope {
//                throw Exception("something bad happened")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace() // <--
//        }
//    }
//    Thread.sleep(1000)
//}

//Вопрос 4
//Ответ на вопрос 4: нет, т.к исключение в соседней корутине произойдет быстрее,
// в рамках coroutineScope при возникновении исключения в одной дочерней корутине, вторая отменяется

//fun main() {
//    CoroutineScope(EmptyCoroutineContext).launch {
//        try {
//            coroutineScope {
//                launch {
//                    delay(500)
//                    throw Exception("something bad 1 happened") // <--
//                }
//                launch {
//                    throw Exception("something bad 2 happened")
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//    Thread.sleep(1000)
//}

//Вопрос 5
//Ответ на вопрос 5: да, в рамках supervisorScope исключения в дочерних корутинах не влияют на соседние дочерние и родительскую

//fun main() {
//    CoroutineScope(EmptyCoroutineContext).launch {
//        try {
//            supervisorScope {
//                launch {
//                    delay(500)
//                    throw Exception("something bad 1 happened") // <--
//                }
//                launch {
//                    throw Exception("something bad 2 happened")
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace() // <--
//        }
//    }
//    Thread.sleep(1000)
//}

//Вопрос 6
//Ответ на вопрос 6: нет, исключение внутри корутины отменяет эту корутину и все дочерние корутины

//fun main() {
//     CoroutineScope(EmptyCoroutineContext).launch {
//        CoroutineScope(EmptyCoroutineContext).launch {
//            launch {
//                //println("start1")
//                delay(1000)
//                println("ok 1") // <--
//            }
//            launch {
//                //println("start2")
//                delay(500)
//                println("ok 2")
//            }
//            throw Exception("something bad happened")
//        }
//    }
//    Thread.sleep(1000)
//}

//Вопрос 7
//Ответ на вопрос 7: нет, исключение произойдет быстрее, корутина отменит себя, следовательно, все дочерние

fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext + SupervisorJob()).launch {
            launch {
                //println("start1")
                delay(1000)
                println("ok 1") // <--
            }
            launch {
                //println("start2")
                delay(500)
                println("ok 2")
            }
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}