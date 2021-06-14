package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.beans.Source;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "314eb01ee5054b7da998359f29285675";  //TODO add your api key

	public void process(String q, Endpoint end, Country cont, Category cat) {
		System.out.println("Start process");

		//TODO implement Error handling

		//TODO load the news based on the parameters

		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ(q)
				.setEndPoint(end)// example of how to use enums
				.setSourceCountry(cont)       // example of how to use enums
				.setSourceCategory(cat) // example of how to use enums
				.createNewsApi(); // liefert ein new NewsApi Objekt zurück

		/*if(newsApi.getApiKey() == null){
			throw new NewsApiException("Api KEY IS UNVLAID");
		}
		if(newsApi.getQ() == null){
			throw new NewsApiException("Q IS UNVLAID");
		}
		if(newsApi.getEndpoint().getValue() == null){
			throw new NewsApiException("Value IS UNVLAID");
		}*/

		NewsResponse newsResponse = (NewsResponse) getData(newsApi);
		try {
			newsResponse = newsApi.getNews();

		}catch (NewsApiException abc) {
			System.out.println("problem1");
		}

		if(newsResponse != null){
			List<Article> articles = newsResponse.getArticles();
			//articles.stream().forEach(article -> System.out.println(article.toString()));

			shortestName(articles);
			numberOfArticles(articles);
			lengthAndTitle(articles);
			providerWithMostArticles(articles);

		}
		System.out.println("End process");
	}
	public static void shortestName(List<Article> articles) {
		String shortest = articles.stream()
				.filter(article -> article.getAuthor() != null)
				.map(Article::getAuthor)
				.min(Comparator.comparing(String::length))
				.get();

		System.out.println("the author with the shortest name is named" + shortest);
		//TODO implement methods for analysis

		System.out.println("End process");
	}

	public static void providerWithMostArticles(List<Article> articles) {
		String provider =
				articles.stream()
						.map(Article::getSource)
						.map(Source::getName)
						.collect(Collectors.groupingBy(Function.identity(),Collectors.counting()))
						.entrySet().stream().max(Map.Entry.comparingByValue())
						.map(Map.Entry::getKey).orElse(null);

		System.out.println("The Newspaper with the most articles is: " + provider);

	}
	public static void numberOfArticles(List<Article> articles) {
		long amount = articles.stream()
				.count();

		System.out.println("The amount of Articles is: " + amount);

	}

	public static void lengthAndTitle (List<Article> articles) {
		articles.stream()
				.map(Article::getTitle)
				.sorted(Comparator.comparing(String::length).reversed())
				.forEach(System.out::println);


	}
	

	public Object getData(NewsApi newsapi) {

		NewsResponse newsResponse = null;
		try {
			newsResponse = newsapi.getNews();

		} catch (NewsApiException abc) {
			System.out.println("problem1");
		}
		return newsResponse;
	}
}
