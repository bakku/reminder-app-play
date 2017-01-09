import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

class UsersControllerSpec extends PlaySpec with OneAppPerSuite {

  "#index" should {

    "return forbidden if no auth header given" in {
      val index = route(app, FakeRequest(GET, "/users")).get
      status(index) mustBe FORBIDDEN
    }

  }

  "#show" should {
    
    "return not found if user does not exist" in {
      val show = route(app, FakeRequest(GET, "/users/1")).get
      status(show) mustBe NOT_FOUND
    }

  }

}
