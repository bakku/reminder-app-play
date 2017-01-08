import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

class UsersControllerSpec extends PlaySpec with OneAppPerSuite {

  "#index" should {

    "get all users when no one created" in {
      val index = route(app, FakeRequest(GET, "/users")).get
      status(index) mustBe OK
      contentType(index) mustBe Some("application/json")
      contentAsString(index) mustBe ("[]")
    }

  }

  "#show" should {
    
    "return not found if user does not exist" in {
      val show = route(app, FakeRequest(GET, "/users/1")).get
      status(show) mustBe NOT_FOUND
    }

  }

}
