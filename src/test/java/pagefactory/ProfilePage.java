package pagefactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v145.profiler.model.Profile;
import org.openqa.selenium.support.FindBy;

public class ProfilePage extends BasePage{

    public ProfilePage(WebDriver givenDriver) {
        super(givbenDriver);
    }

    @FindBy(css = "[data-testid='theme-card-violet']")
    WebElement violetTheme;

    @FindBy(css = "[data-testid='theme-card-violet'][class='theme selected']")
    WebElement selectedVioletTheme;

    public boolean isVioletThemeSelected(){
        findElement(selectedVioletTheme);
        return selectedVioletTheme.isDisplayed();
    }

    public ProfilePage chooseVioletTheme(){
        click(violetTheme);
        return this;
    }
}
