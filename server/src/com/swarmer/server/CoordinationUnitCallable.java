package com.swarmer.server;

import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Callable;
import com.swarmer.shared.communication.IPGetter;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.net.Socket;

public class CoordinationUnitCallable extends Callable {
    public CoordinationUnitCallable(Message message) throws IOException {
        super(new Socket(IPGetter.getInstance().getDatabaseControllerIP(), ServerUnit.COORDINATE_UNIT_TCP_PORT), message);
    }
}
