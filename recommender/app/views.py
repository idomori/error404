from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
import json
import os
import requests
import spotipy
from spotipy import oauth2
from spotipy.oauth2 import SpotifyClientCredentials
import urllib.parse as urlparse
from urllib.parse import parse_qs
from scipy import spatial
from numpy import dot
from numpy.linalg import norm


from django.db import connection
# Added to make recommender Import not working for some reason. I installed pandas
import pandas as pd
import random

os.environ['SPOTIPY_CLIENT_ID']='7a03c05c75c04958926b5213cda242f3'
os.environ['SPOTIPY_CLIENT_SECRET']='6d50816451cf45f4a7b09a0188f30fab'




@csrf_exempt
def update_rec(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    track_id_0 = request.GET.get("track_id_0")
    track_id_1 = request.GET.get("track_id_1")
    track_id_2 = request.GET.get("track_id_2")
    track_id_3 = request.GET.get("track_id_3")
    track_id_4 = request.GET.get("track_id_4")


    track_id_list = []
    if track_id_0 is not None:
        track_id_list.append(track_id_0)
    if track_id_1 is not None:
        track_id_list.append(track_id_1)
    if track_id_2 is not None:
        track_id_list.append(track_id_2)
    if track_id_3 is not None:
        track_id_list.append(track_id_3)
    if track_id_4 is not None:
        track_id_list.append(track_id_4)
    
    min_key = request.GET.get("min_key")
    max_key = request.GET.get("max_key")
    min_danceability = request.GET.get("min_danceability")
    max_danceability = request.GET.get("max_danceability")
    min_energy = request.GET.get("min_energy")
    max_energy = request.GET.get("max_energy")
    min_valence = request.GET.get("min_valence")
    max_valence = request.GET.get("max_valence")
    min_tempo = request.GET.get("min_tempo")
    max_tempo = request.GET.get("max_tempo")

    #print("TEMPO")
    #print(max_tempo)
    #print(min_tempo)

    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability", "energy", "valence"]
    list_info = {}

    list_info["tracks"] = []
    
    tracks = sp.tracks(track_id_list)
    for track in tracks["tracks"]:
        track_info = {}
        track_info["track_name"] = track["name"]
        track_info["track_id"] = track["id"]
        track_info["artist"] = track["artists"][0]["name"]
        track_info["artist_id"] = track["artists"][0]["id"]
        list_info["tracks"].append(track_info)
    
       
    #These print statements show the songs seeding the recommender when it becomes time to update the weights
    #The old seeding is ignored and these new songs are used to seed the recommender. Their weights are used to find similar songs
    
    print("New seeding tracks:")
    print("#1 ", list_info["tracks"][0]["track_id"])
    print("#2 ", list_info["tracks"][1]["track_id"])
    print("#3 ", list_info["tracks"][2]["track_id"])
    print("#4 ", list_info["tracks"][3]["track_id"])
    print("#5 ", list_info["tracks"][4]["track_id"])

    list_af = sp.audio_features(track_id_list)

    for track_af, track in zip(list_af, list_info["tracks"]):
        for feature in playlist_items[6:]:
            track[feature] = track_af[feature]

    all_recs = update_rec_helper(track_id_list, min_key, max_key, min_danceability, max_danceability, min_energy, max_energy, min_valence, max_valence, min_tempo, max_tempo)
    
    response = {}
    response["result"] = nearest_neighbors(list_info, all_recs)
    return JsonResponse(response)



@csrf_exempt
def update_rec_helper(list, min_key, max_key, min_danceability, max_danceability, min_energy, max_energy, min_valence, max_valence, min_tempo, max_tempo):
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)

    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability", "energy", "valence"]

    

    rec = sp.recommendations(seed_tracks = list, limit=30, min_key = min_key, max_key = max_key, min_danceability = min_danceability, 
                             max_danceability = max_danceability, min_energy = min_energy, max_energy = max_energy, min_valence = min_valence , max_valence = max_valence, 
                             min_tempo = min_tempo, max_tempo = max_tempo)

    all_recs = {}
    all_recs["recommendation"] = []

    rec_track_id_list = []
    for rec_track in rec["tracks"]:
        rec_info = {}
        rec_info["track_name"] = rec_track["name"]
        rec_info["artist"] = rec_track["album"]["artists"][0]["name"]
        rec_info["track_id"] = rec_track["id"]
        rec_info["artist_id"] = rec_track["album"]["artists"][0]["id"]
        rec_info["preview_url"] = rec_track["preview_url"]#null?
        if rec_track["album"]["images"][0]["url"] is not None:
            rec_info["image_url"] = rec_track["album"]["images"][0]["url"]
        else:
            rec_info["image_url"] = None
        rec_track_id_list.append(rec_track["id"])
        all_recs["recommendation"].append(rec_info)

    max_amount = min(len(rec_track_id_list), 100)

    rec_feat_list = sp.audio_features(rec_track_id_list[:max_amount])
    
    for track_af, track in zip(rec_feat_list, all_recs["recommendation"]):
        for feature in playlist_items[6:]:
            track[feature] = track_af[feature]

    return all_recs

@csrf_exempt
def nearest_neighbors(initial_seed, initial_recs):
		# calculate size of input playlist 
	input_size = len(initial_seed["tracks"]) # 10 is length of input dictonary 
	# take the average for each paramater in input playlist 
	temp_avg = 0
	energy_avg = 0
	key_avg = 0
	dance_avg = 0
	valence_avg = 0

	for song in initial_seed["tracks"]: #playlist is the input dictioanary
	    # need to check if the paramter is wanted and if not it just stays at 0 for all songs
	    # if tempo exits
	    temp_avg = temp_avg + song["tempo"]
	    # if energy exits
	    energy_avg = energy_avg + song["energy"]
	    # if key exits
	    key_avg = key_avg + song["key"]
	    # if dance exits
	    dance_avg = dance_avg + song["danceability"]
	    # if valence exits
	    valence_avg = valence_avg + song["valence"]

	temp_avg = temp_avg / input_size
	energy_avg = energy_avg / input_size
	key_avg = key_avg / input_size
	dance_avg = dance_avg / input_size
	valence_avg = valence_avg / input_size
	# vectorize it (A)
	input_vector = [temp_avg, energy_avg, key_avg, dance_avg, valence_avg]
	spotify_list_vectors = [] # this a list of tuples formatted as (track_id, [tempo, energy, key, dance])
	N = len(initial_recs["recommendation"]) # size of spotify_playlist
	# Now for spotify playlist for each song vectorize paramaters
	for song in initial_recs["recommendation"]: # spotify playlist is the the spotify song dictionary 
	    # need to chosoe which paramters to put in
	    vector_temp = [song["tempo"], song["energy"], song["key"], song["danceability"], song["valence"]]
	    tuple_entry = (song["track_name"], song["artist"], song["track_id"], song["artist_id"], song["preview_url"], song["image_url"], song["tempo"], song["energy"], song["key"], song["danceability"], song["valence"], vector_temp)
	    spotify_list_vectors.append(tuple_entry)
	    # vector_temp.clear()
	    #N = N + 1
	    # check to make sure that clearing the vector doesnt clear it in the tuple and thus the spotify list
	# above is prep work
	# tuple of track id and then vector for the N vectors (not A)
	# Get total amount of vectors from spotify (N)
	cosine_list = []
	for i in range(N):
	    # take cosine similraty between A and each vector 1-N
	    # print(input_vector)
	    # print(spotify_list_vectors[i])
	    # print(spotify_list_vectors[i][4])
	    cos_sim = 1 - spatial.distance.cosine(input_vector, spotify_list_vectors[i][11])
            # cos_sim = dot(input_vector, spotify_list_vectors[i][11]) / (norm(input_vector) * norm(spotify_list_vectors[i][11]))
	    # keep track of track_id
	    cosine_tuple = (spotify_list_vectors[i][0], spotify_list_vectors[i][1], spotify_list_vectors[i][2], spotify_list_vectors[i][3], spotify_list_vectors[i][4], spotify_list_vectors[i][5], spotify_list_vectors[i][6], spotify_list_vectors[i][7], spotify_list_vectors[i][8],spotify_list_vectors[i][9], spotify_list_vectors[i][10], cos_sim)
	    # store in a list the rating
	    cosine_list.append(cosine_tuple)
	# sort list by cosine similarity and then sort by high to low (so most similar is cosine_list[0])
	cosine_list.sort(reverse=True, key = lambda x: x[11])
	# above is cosine similarty
	new_list = []
	#print("Variable N: ")
	#print(N)
	for j in range(0, int(N / 2)):
		temp_dict = {}
		temp_dict["track_name"] = cosine_list[j][0]
		temp_dict["artist"] = cosine_list[j][1]
		temp_dict["track_id"] = cosine_list[j][2]
		temp_dict["artist_id"] = cosine_list[j][3]
		temp_dict["preview_url"] = cosine_list[j][4]
		temp_dict["image_url"] = cosine_list[j][5]
		temp_dict["tempo"] = cosine_list[j][6]
		temp_dict["energy"] = cosine_list[j][7]
		temp_dict["key"] = cosine_list[j][8]
		temp_dict["danceability"] = cosine_list[j][9]
		temp_dict["valence"] = cosine_list[j][10]
		new_list.append(temp_dict)
	return new_list
	# if paramater is turned off, need to just put the wanted ones in for the spotify vector temp

@csrf_exempt
def playlist_rec_helper(seed_list, min_key, max_key, min_danceability, max_danceability, min_energy, max_energy, min_valence, max_valence, min_tempo, max_tempo):
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)

    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability", "energy", "valence"]

    artists = []
    tracks = []
    playlist = sp.playlist_tracks(seed_list)
    for index in playlist["items"]:
        artists.append(index["track"]["album"]["artists"][0]["id"])
        tracks.append(index["track"]["id"])
   
    all_recs = {}
    all_recs["recommendation"] = []
    rec_track_id_list = []
    num_samples = random.sample(range(0, len(tracks)), 5)
    #print(num_samples)
    sample_tracks = []
    for i in num_samples:
        sample_tracks.append(tracks[i])


    rec = sp.recommendations(seed_tracks = sample_tracks, limit=30, min_key = min_key, max_key = max_key, min_danceability = min_danceability, 
                             max_danceability = max_danceability, min_energy = min_energy, max_energy = max_energy, min_valence = min_valence , max_valence = max_valence, 
                             min_tempo = min_tempo, max_tempo = max_tempo)

    for rec_track in rec["tracks"]:
        rec_info = {}
        rec_info["track_name"] = rec_track["name"]
        rec_info["artist"] = rec_track["album"]["artists"][0]["name"]
        rec_info["track_id"] = rec_track["id"]
        rec_info["artist_id"] = rec_track["album"]["artists"][0]["id"]
        rec_info["preview_url"] = rec_track["preview_url"]#null?
        if rec_track["album"]["images"][0]["url"] is not None:
            rec_info["image_url"] = rec_track["album"]["images"][0]["url"]
        else:
            rec_info["image_url"] = None
        rec_track_id_list.append(rec_track["id"])
        all_recs["recommendation"].append(rec_info)

    max_amount = min(len(rec_track_id_list), 100)

    rec_feat_list = sp.audio_features(rec_track_id_list[:max_amount])
    
    for track_af, track in zip(rec_feat_list, all_recs["recommendation"]):
        for feature in playlist_items[6:]:
            track[feature] = track_af[feature]


    return all_recs

@csrf_exempt
def track_rec_helper(seed_list, min_key, max_key, min_danceability, max_danceability, min_energy, max_energy, min_valence, max_valence, min_tempo, max_tempo):
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)

    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability", "energy", "valence"]
    seed = [seed_list]
    rec_info = {}
    rec = sp.recommendations(seed_tracks = seed, limit = 5, min_key = min_key, max_key = max_key, min_danceability = min_danceability, 
                             max_danceability = max_danceability, min_energy = min_energy, max_energy = max_energy, min_valence = min_valence , max_valence = max_valence, 
                             min_tempo = min_tempo, max_tempo = max_tempo)

    all_recs = {}
    all_recs["recommendation"] = []
    rec_track_id_list = []
    for rec_track in rec["tracks"]:
        rec_info = {}
        rec_info["track_name"] = rec_track["name"]
        rec_info["artist"] = rec_track["album"]["artists"][0]["name"]
        rec_info["track_id"] = rec_track["id"]
        rec_info["artist_id"] = rec_track["album"]["artists"][0]["id"]
        rec_info["preview_url"] = rec_track["preview_url"]#null?
        if rec_track["album"]["images"][0]["url"] is not None:
            rec_info["image_url"] = rec_track["album"]["images"][0]["url"]
        else:
            rec_info["image_url"] = None
        rec_track_id_list.append(rec_track["id"])
        all_recs["recommendation"].append(rec_info)
    max_amount = min(len(rec_track_id_list), 100)

    rec_feat_list = sp.audio_features(rec_track_id_list[:max_amount])
    
    for track_af, track in zip(rec_feat_list, all_recs["recommendation"]):
        for feature in playlist_items[6:]:
            track[feature] = track_af[feature]

    return all_recs

def getsongs(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)  
    track_id = '6rqhFgbbKwnb9MLmUQDhG6'  # Dummy Track
    response = {}
    response['songs'] = sp.audio_features([track_id])
    return JsonResponse(response)


@csrf_exempt
def postsong(request):

    if request.method != 'POST':
        return HttpResponse(status=404)
    #print(request)
    json_data = json.loads(request.body)
    track_id = json_data['track_id']

    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
    token = auth_manager.get_access_token()
    

    track_info = {}
    track_info['songs'] = sp.audio_features([track_id])
    
    ## https://api.spotify.com/v1/tracks/{id}
    id = track_info['songs'][0]['id']
    try:
    	response = requests.get(url='https://api.spotify.com/v1/tracks/' + id, 
                              	headers={'Authorization': 'Bearer '	 + token})
    except requests.exceptions.RequestException as e:  # This is the correct syntax
    	print(e)
	
    response = response.json()
    track_info['songs'][0]['artists'] = response['artists']
    track_info['songs'][0]['name'] = response['name']
    

    return JsonResponse(track_info)

@csrf_exempt
def getsong(request):

    if request.method != 'GET':
        return HttpResponse(status=404)
    #print(request)
    track_id = request.GET.get("track_id")
    min_key = request.GET.get("min_key")
    max_key = request.GET.get("max_key")
    min_danceability = request.GET.get("min_danceability")
    max_danceability = request.GET.get("max_danceability")
    min_energy = request.GET.get("min_energy")
    max_energy = request.GET.get("max_energy")
    min_valence = request.GET.get("min_valence")
    max_valence = request.GET.get("max_valence")
    min_tempo = request.GET.get("min_tempo")
    max_tempo = request.GET.get("max_tempo")

    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
    token = auth_manager.get_access_token()
    
    #print("TEMPO")
    #print(min_tempo)
    #print(max_tempo)

    
    track_info = {}
    sp_response = sp.audio_features([track_id])
    track_info["tracks"] = []
    info = {}
    info["danceability"] = sp_response[0]["danceability"]
    info["tempo"] = sp_response[0]["tempo"]
    info["key"] = sp_response[0]["key"]
    info["valence"] = sp_response[0]["valence"]
    info["energy"] = sp_response[0]["energy"]
    info["track_id"] = sp_response[0]["id"]

	  
    print("Features of Song Submitted by User")
    print("key: ", info["key"])
    print("bpm: ", info["tempo"])
    print("energy: ", info["energy"])
    print("danceability: ", info["danceability"])

    track_info["tracks"].append(info)


    
    ## https://api.spotify.com/v1/tracks/{id}
    id = track_info['tracks'][0]['track_id']
    try:
    	response = requests.get(url='https://api.spotify.com/v1/tracks/' + id, 
                              	headers={'Authorization': 'Bearer '	 + token})
    except requests.exceptions.RequestException as e:  # This is the correct syntax
    	print(e)
   ##  print(response.json())

    response = response.json()
    track_info['tracks'][0]['artist'] = response['artists'][0]['name']
    track_info['tracks'][0]['artist_id'] = response['artists'][0]['id']
    track_info['tracks'][0]['track_name'] = response['name']

    all_recs = track_rec_helper(track_id, min_key, max_key, min_danceability, max_danceability, min_energy, max_energy, min_valence, max_valence, min_tempo, max_tempo)

    response = {}
    response["result"] = nearest_neighbors(track_info, all_recs)
    #print(response)
	  
    #This is commented out because printing gunicorn status has a limited number of lines
    #These print statements are where we showed the recommended song has similar key, bpm, energy, and danceability to the seeded song 
    print("Recommended song")
    print("key: ", response["result"][0]["key"])
    print("bpm: ", response["result"][0]["tempo"])
    print("energy: ", resposne["result"][0]["energy"])
    print("danceability: ", response["result"][0]["danceability"])
	  
    #These print statements are where we printed the track id for the seeded song (the song the user inputs)
    #and also show the track ids for five recommended songs. These five ids are later seen from another print
    #statement as input to the recommender. We use features from the songs seeding the recommender as the weights (key, bpm, etc)
    print("Track id of song seeding recommender: ", info["track_id"])
    print("Track id of recommended song: ")
    print("#0 ", response["result"][0]["track_id"])
    print("#1 ", response["result"][1]["track_id"])
    print("#2 ", response["result"][2]["track_id"])
    print("#3 ", response["result"][3]["track_id"])
    print("#4 ", response["result"][4]["track_id"])
    
    return JsonResponse(response)




@csrf_exempt
def read_playlist(request):
    
    if request.method != 'GET':
        return HttpResponse(status=404)

    #print("READ_PLAYLIST IS BEING CALLED")
    pid = request.GET.get("playlist_id")
    min_key = request.GET.get("min_key")
    max_key = request.GET.get("max_key")
    min_danceability = request.GET.get("min_danceability")
    max_danceability = request.GET.get("max_danceability")
    min_energy = request.GET.get("min_energy")
    max_energy = request.GET.get("max_energy")
    min_valence = request.GET.get("min_valence")
    max_valence = request.GET.get("max_valence")
    min_tempo = request.GET.get("min_tempo")
    max_tempo = request.GET.get("max_tempo")
    #print("TEMPO")
    #print(min_tempo)
    #print(max_tempo)


    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)

    

    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability", "energy", "valence"]

    playlist_info = {}
    playlist_info["tracks"] = []
    playlist = sp.playlist_tracks(pid)

    
    playlist_tracks = []


    for track in playlist["items"]:
        playlist_feat = {}
	
        playlist_feat["track_name"] = track["track"]["name"]
        playlist_feat["artist"] = track["track"]["album"]["artists"][0]["name"]
        playlist_feat["track_id"] = track["track"]["id"]
        playlist_feat["artist_id"] = track["track"]["album"]["artists"][0]["id"]
        playlist_feat["preview_url"] = track["track"]["preview_url"]
        playlist_feat["image_url"] = track["track"]["album"]["images"][0]["url"]


        playlist_info["tracks"].append(playlist_feat)
        playlist_tracks.append(track["track"]["id"]) # Adding to this to get audio features later

    # In case that the playlist has more than 100 tracks
    max_amount = min(len(playlist_tracks), 100)

    playlist_af = sp.audio_features(playlist_tracks[:max_amount])
    for track_af, track in zip(playlist_af, playlist_info["tracks"]):
        for feature in playlist_items[6:]:
            track[feature] = track_af[feature]

    all_recs = playlist_rec_helper(pid, min_key, max_key, min_danceability, max_danceability, min_energy, max_energy, min_valence, max_valence, min_tempo, max_tempo)

    response = {}
    response["result"] = nearest_neighbors(playlist_info, all_recs)

    return JsonResponse(response)


@csrf_exempt
def make_rec(json_input):
    if json_input.method != 'GET':
        return HttpResponse(status=404)
    json_data = json.loads(json_input.body)
    seed_list = json_data['playlist_id']
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
   
    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability"]

    artists = []
    tracks = []
    playlist = sp.playlist_tracks(seed_list)
    for index in playlist["items"]:
        artists.append(index["track"]["album"]["artists"][0]["id"])
        tracks.append(index["track"]["id"])
   
    recommend = {}
    rec_info = {}

    for i in range(5,len(playlist)+1,5):
        rec = sp.recommendations(seed_tracks = tracks[i-5:i], limit=25)
        rec_info["track_name"] = rec["tracks"][0]["name"]
        rec_info["artist"] = rec["tracks"][0]["album"]["artists"][0]["name"]
        rec_info["track_id"] = rec["tracks"][0]["id"]
        rec_info["artist_id"] = rec["tracks"][0]["album"]["artists"][0]["id"]
        rec_info["preview_url"] = rec["tracks"][0]["preview_url"]#null?
        rec_info["image_url"] = rec["tracks"][0]["album"]["images"][0]["url"]
        rec_feat = sp.audio_features(rec_info["track_id"])[0]

        for feature in playlist_items[6:]:
            rec_info[feature] = rec_feat[feature]


    return JsonResponse(rec_info)

@csrf_exempt
def search_playlist(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    playlist_name = request.GET.get("search_param")
    find_type = request.GET.get("search_for")

    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)

    results = {}
    ret = {}
    ret["cover"] = []
    ret["playlist_name"] = []
    ret["playlist_id"] = []
    return_dict = {}
    return_dict["result"] = []

    results = sp.search(q=playlist_name,type=find_type, limit="50")

    if find_type == "playlist":
        for playlist in results["playlists"]["items"]:
            ret["cover"] = playlist["images"][0]["url"]
            ret["playlist_name"] = playlist["name"]
            ret["playlist_id"] = playlist["external_urls"]["spotify"]
            return_dict["result"].append([ret["cover"], ret["playlist_name"], ret["playlist_id"]])

    elif find_type == "track":
        for song in results["tracks"]["items"]:
            ret["cover"] = song["album"]["images"][0]["url"]
            ret["playlist_name"] = song["name"]
            ret["playlist_id"] = song["external_urls"]["spotify"]
            return_dict["result"].append([ret["cover"], ret["playlist_name"], ret["playlist_id"]])


    return JsonResponse(return_dict)
