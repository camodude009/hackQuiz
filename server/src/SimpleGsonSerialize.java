import model.ServiceRequestPacket;

public class SimpleGsonSerialize {

    public static void main(String[] args) {

        ServiceRequestPacket sr = new ServiceRequestPacket(ServiceRequestPacket.CALL_BARKEEPER);

        String jsonSend = Serializer.serializeObject(sr);
        System.out.println(jsonSend);

        //---Server com---
        String jsonReceived = jsonSend;
        //--Server com---

        String token = Serializer.getTokenFromPacket(jsonReceived);
        switch (token) {
            case ServiceRequestPacket.token:
                ServiceRequestPacket exampleServiceRequest = (ServiceRequestPacket) Serializer.deserializePacket(jsonReceived, ServiceRequestPacket.class);
                System.out.println(exampleServiceRequest); //Do sth
        }


    }
}
