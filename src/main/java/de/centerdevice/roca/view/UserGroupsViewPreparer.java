package de.centerdevice.roca.view;

import de.centerdevice.roca.centerdevice.CenterDeviceService;
import de.centerdevice.roca.domain.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.springframework.beans.factory.annotation.Autowired;

public class UserGroupsViewPreparer implements ViewPreparer {

    @Autowired
    private CenterDeviceService centerdevice;

    @Override
    public void execute(Request tilesContext, AttributeContext attributeContext) {
        User userInformation = new User();
        try {
            userInformation = centerdevice.getUserInformation("");
        } catch (IOException ex) {
            Logger.getLogger(UserGroupsViewPreparer.class.getName()).log(Level.SEVERE, null, ex);
        }

        tilesContext.getContext("request").put("user", userInformation);
    }
}
