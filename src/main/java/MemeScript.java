import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class MemeScript {



    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<List<Repo>> repos = service.listRepos("NByida");
        repos.enqueue(new Callback<List<Repo>>(){
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                System.out.println("onResponse" +response.body());
            }

            public void onFailure(Call<List<Repo>> call, Throwable throwable) {
                System.out.println(" onFailure" +throwable.getCause().toString());
            }
        });
    }

}
