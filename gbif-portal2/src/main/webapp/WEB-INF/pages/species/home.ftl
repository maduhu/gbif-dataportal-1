<html>
<head>
  <title>Species Search</title>
  <meta name="menu" content="species"/>
</head>
<body class="dataset">

  <article class="dataset">
    <header></header>
    <div class="content">
      <h1>Search through 121,201 species</h1>

      <form action="/species/search">
        <span class="input_text">
          <input type="text" name="q" placeholder="Search species by name, taxon, place..."/>
        </span>
        <button type="submit" class="search_button"><span>Search</span></button>
      </form>
      <ul class="species">
        <li><a href="/species/search?q=birds" title="Birds">Birds</a></li>
        <li><a href="/species/search?q=butterflies" title="Butterflies">Butterflies</a></li>
        <li><a href="/species/search?q=lizards" title="Lizards">Lizards</a></li>
        <li><a href="/species/search?q=reptiles" title="Reptiles">Reptiles</a></li>
        <li><a href="/species/search?q=fishes" title="Fishes">Fishes</a></li>
        <li><a href="/species/search?q=mammals" title="Mammals">Mammals</a></li>
        <li><a href="/species/search?q=insects" title="Insects">Insects</a></li>
      </ul>
      <div class="results">
        <ul>
          <li><a href="/species/search?q=taxa" title="">2,183,212</a>total taxon</li>
          <li><a href="/species/search?q=species" title="">1,291,282</a>total species</li>
          <li class="last"><a href="/species/search?q=usage" title="">121,251</a>name usages</li>
        </ul>
      </div>
    </div>
    <footer></footer>
  </article>
  <p class="advice">Looking for something more specific? Use our <a href="#" title="advance filtering">advanced
    filtering</a> or the <a href="#" title="taxonomic browser">taxonomic browser</a>.</p>


</body>
</html>
