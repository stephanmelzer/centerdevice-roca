package de.centerdevice.roca.view;

import de.centerdevice.roca.centerdevice.CenterDeviceService;
import de.centerdevice.roca.domain.Group;
import de.centerdevice.roca.domain.User;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

        List<Group> groups = userInformation.getGroups();
        Group globalGroup = new Group();
        int globalGroupIndex = 0;
        boolean foundGlobalGroup = false;

        for (Group group : groups) {
            if (group.getId().contains("ALL_USERS")) {
                globalGroup = group;
                foundGlobalGroup = true;
                break;
            }

            globalGroupIndex++;
        }

        if (foundGlobalGroup) {
            globalGroup.setName("Alle");
            groups.remove(globalGroupIndex);
        }

        Collections.sort(groups, new Comparator<Group>() {
            @Override
            public int compare(Group group1, Group group2) {
                return group1.getName().compareTo(group2.getName());
            }
        });

        tilesContext.getContext("request").put("user", userInformation);
        tilesContext.getContext("request").put("globalGroup", globalGroup);
    }
}
