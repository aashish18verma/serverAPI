package io.getarrays.server.service.implementaion;

import io.getarrays.server.enumeration.Status;
import io.getarrays.server.model.Server;
import io.getarrays.server.repo.ServerRepo;
import io.getarrays.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.Servlet;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Random;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final ServerRepo serverRepo;
    @Override
    public Server create(Server server) {
        log.info("Saving new Server: {}", server);
        server.setImageUrl(setServerImageUrl());
        return serverRepo.save(server);
    }

    private String setServerImageUrl() {
        String [] imageNames = {"server1.png", "server2.png", "server3.png", "server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/"+imageNames[new Random().nextInt(4)]).toUriString();
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
       log.info("pinging server ip: {}", ipAddress);
       Server server = serverRepo.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000 ) ? Status.SERVER_DOWN: Status.SERVER_DOWN);
        serverRepo.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
      log.info("Feaching All the server");
      return serverRepo.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public Server get(Long id) {
       log.info("Fetching server by id: {}", id);
       return serverRepo.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("updating server: {}", server.getName());
        return serverRepo.save(server);
    }

    @Override
    public Boolean delete(Long id) {
       log.info("deleting server by id : {}", id);
       serverRepo.deleteById(id);
        return Boolean.TRUE;
    }
}
